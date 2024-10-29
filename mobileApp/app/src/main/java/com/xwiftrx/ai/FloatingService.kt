package com.xwiftrx.ai

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

class FloatingService : LifecycleService() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var viewModel: ChatViewModel
    private val viewModelStore: ViewModelStore = ViewModelStore()
    private lateinit var focusableParams: WindowManager.LayoutParams
    private lateinit var nonFocusableParams: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            stopSelf()
            return
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null)

        focusableParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
        }

        nonFocusableParams = WindowManager.LayoutParams(
            60, 60, // Compact initial dimensions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
        }

        windowManager.addView(floatingView, nonFocusableParams)

        val openOverlayIcon = floatingView.findViewById<ImageView>(R.id.open_overlay_icon)
        val closeOverlayIcon = floatingView.findViewById<ImageView>(R.id.close_overlay_icon)
        val overlayContent = floatingView.findViewById<RelativeLayout>(R.id.overlay_content)
        val promptInput = floatingView.findViewById<EditText>(R.id.prompt_input)
        val sendButton = floatingView.findViewById<Button>(R.id.send_button)
        val responseText = floatingView.findViewById<TextView>(R.id.response_text)
        val scrollView = floatingView.findViewById<ScrollView>(R.id.response_scrollview)

        viewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ChatViewModel::class.java]

        sendButton.setOnClickListener {
            val input = promptInput.text.toString()
            if (input.isNotEmpty()) {
                sendPrompt(input)
                promptInput.text.clear()
            }
        }

        viewModel.aiResponse.observe(this, Observer { response ->
            response?.let {
                responseText.text = it
                scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
            }
        })

        openOverlayIcon.setOnClickListener {
            openOverlayIcon.visibility = View.GONE
            overlayContent.visibility = View.VISIBLE
            windowManager.updateViewLayout(floatingView, focusableParams)  // Switch to expanded view
        }

        closeOverlayIcon.setOnClickListener {
            overlayContent.visibility = View.GONE
            openOverlayIcon.visibility = View.VISIBLE
            windowManager.updateViewLayout(floatingView, nonFocusableParams)  // Switch to compact view
        }

        promptInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(promptInput, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        // Implement drag functionality
        floatingView.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = nonFocusableParams.x
                        initialY = nonFocusableParams.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        nonFocusableParams.x = initialX + (event.rawX - initialTouchX).toInt()
                        nonFocusableParams.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(floatingView, nonFocusableParams)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView.isAttachedToWindow) {
            windowManager.removeView(floatingView)
        }
        viewModelStore.clear()
    }

    private fun sendPrompt(prompt: String) {
        viewModel.sendMessageToAI(prompt)
    }
}

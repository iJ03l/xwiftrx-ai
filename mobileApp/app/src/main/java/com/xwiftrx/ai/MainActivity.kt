package com.xwiftrx.ai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var typingIndicator: TextView
    private val chatMessages = mutableListOf<ChatMessage>()

    private val viewModel: ChatViewModel by viewModels()
    private val REQUEST_OVERLAY_PERMISSION: Int = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        inputEditText = findViewById(R.id.inputEditText)
        sendButton = findViewById(R.id.sendButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        typingIndicator = findViewById(R.id.typingIndicator)

        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.aiResponse.observe(this, Observer { response ->
            val aiMessage = response?.let { ChatMessage(it, false) }
            if (aiMessage != null) {
                chatMessages.add(aiMessage)
                chatAdapter.notifyItemInserted(chatMessages.size - 1)
                chatRecyclerView.scrollToPosition(chatMessages.size - 1)
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                loadingIndicator.visibility = View.VISIBLE
                typingIndicator.visibility = View.VISIBLE // Optional, use only one indicator
            } else {
                loadingIndicator.visibility = View.GONE
                typingIndicator.visibility = View.GONE
            }
        })

        sendButton.setOnClickListener {
            val message = inputEditText.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
                inputEditText.text.clear()
            }
        }

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        } else {
            startFloatingService()
        }
    }

    private fun sendMessage(message: String) {
        val userMessage = ChatMessage(message, true)
        chatMessages.add(userMessage)
        chatAdapter.notifyItemInserted(chatMessages.size - 1)

        viewModel.sendMessageToAI(message)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OVERLAY_PERMISSION && Settings.canDrawOverlays(this)) {
            startFloatingService()
        }
    }

    private fun startFloatingService() {
        val intent = Intent(this, FloatingService::class.java)
        startService(intent)
    }
}

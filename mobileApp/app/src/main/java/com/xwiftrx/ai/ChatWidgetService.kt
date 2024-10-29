package com.xwiftrx.ai

import android.app.IntentService
import android.content.Intent
import android.widget.Toast

class ChatWidgetService : IntentService("ChatWidgetService") {

    companion object {
        const val ACTION_SEND_TEST_MESSAGE = "com.xwiftrx.ai.action.SEND_TEST_MESSAGE"
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == ACTION_SEND_TEST_MESSAGE) {
            sendTestMessage()
        }
    }

    private fun sendTestMessage() {
        // Use a static message for demonstration
        val message = StaticData.sampleUserMessages.random()
        val response = StaticData.sampleBotResponses.random()

        // Display a Toast message as a placeholder for sending a real message
        Toast.makeText(this, "Test message: $message\nBot response: $response", Toast.LENGTH_LONG).show()
    }
}
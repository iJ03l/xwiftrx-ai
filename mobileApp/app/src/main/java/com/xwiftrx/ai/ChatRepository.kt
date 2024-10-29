package com.xwiftrx.ai

import com.xwiftrx.ai.AIRequest
import com.xwiftrx.ai.RetrofitClient
import retrofit2.await

class ChatRepository {

    suspend fun sendMessageToAI(message: String): String? {
        return try {
            val request = AIRequest(prompt = message)
            val response = RetrofitClient.apiService.sendMessage(request).await()
            response.response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

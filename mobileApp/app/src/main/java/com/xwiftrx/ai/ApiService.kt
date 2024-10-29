package com.xwiftrx.ai

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class AIRequest(val prompt: String)
data class AIResponse(val response: String)

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("http://127.0.0.1:5000")
    fun sendMessage(@Body request: AIRequest): Call<AIResponse>

    @Headers("Content-Type: application/json")
    @POST("http://127.0.0.1:5000")
    suspend fun sendMessageAsync(@Body request: AIRequest): AIResponse
}

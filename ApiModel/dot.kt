import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit = Retrofit.Builder()
    .baseUrl("http://127.0.0.1:5000")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

val instruction = AIInstruction("send 10 NEAR to receiver.near")
val call = apiService.sendInstruction(instruction)

call.enqueue(object : Callback<AIResponse> {
    override fun onResponse(call: Call<AIResponse>, response: Response<AIResponse>) {
        if (response.isSuccessful) {
            // Handle successful response
        }
    }

    override fun onFailure(call: Call<AIResponse>, t: Throwable) {
        // Handle failure
    }
})

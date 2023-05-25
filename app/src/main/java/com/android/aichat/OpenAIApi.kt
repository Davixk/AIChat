package com.android.aichat

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApi {
    @POST("v1/engines/gpt-4/completions")
    fun createChatCompletion(@Body request: OpenAIRequest): Call<OpenAIResponse>
}

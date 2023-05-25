package com.android.aichat

data class OpenAIResponse(val id: String, val `object`: String, val created: Int, val model: String, val choices: List<Choice>)
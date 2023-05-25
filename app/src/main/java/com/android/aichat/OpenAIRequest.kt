package com.android.aichat

data class OpenAIRequest(val model: String, val messages: List<Message>)
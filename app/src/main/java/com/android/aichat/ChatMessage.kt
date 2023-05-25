package com.android.aichat

data class ChatMessage(val message: String, val sender: Sender)

enum class Sender {
    USER,
    AI
}

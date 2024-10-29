package com.xwiftrx.ai

data class Message(
    val text: String,
    val type: MessageType
)

enum class MessageType {
    USER,
    AI
}

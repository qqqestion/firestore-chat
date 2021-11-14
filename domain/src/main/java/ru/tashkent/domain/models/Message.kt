package ru.tashkent.domain.models

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timeSent: Long,
    val fromCurrentUser: Boolean
)

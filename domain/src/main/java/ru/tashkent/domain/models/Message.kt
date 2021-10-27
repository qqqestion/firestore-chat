package ru.tashkent.domain.models

data class Message(
    val id: String,
    val chatId: String,
    val sender: String,
    val text: String,
    val timeSent: Long,
    val fromCurrentUser: Boolean
)

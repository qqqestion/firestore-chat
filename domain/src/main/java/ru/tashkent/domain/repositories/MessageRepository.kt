package ru.tashkent.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.tashkent.domain.models.Message

interface MessageRepository {

    val messages: Flow<Message>

    fun initMessages(chatId: String, lastMessageTimeSent: Long)

    suspend fun getMessagesByChatId(chatId: String): Result<List<Message>>

    suspend fun sendMessage(chatId: String, messageText: String)

    suspend fun deleteMessagesInChat(chatId: String)
}
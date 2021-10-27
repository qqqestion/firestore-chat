package ru.tashkent.messenger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tashkent.data.repositories.MessageRepository
import ru.tashkent.domain.models.Message

class ChatViewModel : ViewModel() {

    private val repository = MessageRepository()

    val newMessages = repository.messages

    private val messagesData = MutableSharedFlow<List<Message>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages = messagesData.asSharedFlow()

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            repository.getMessagesByChatId(chatId)
                .fold({ handleMessages(chatId, it) }, ::handleMessagesFailure)
        }
    }

    private fun handleMessages(chatId: String, messages: List<Message>) {
        val message = messages.lastOrNull()
        messagesData.tryEmit(messages)
        repository.initMessages(chatId, message?.timeSent ?: 0L)
    }

    private fun handleMessagesFailure(throwable: Throwable) {
    }

    fun sendMessage(chatId: String, messageText: String) {
        viewModelScope.launch {
            repository.sendMessage(chatId, messageText)
        }
    }

    fun deleteMessages(chatId: String) {
        viewModelScope.launch {
            repository.deleteMessagesInChat(chatId)
        }
    }
}
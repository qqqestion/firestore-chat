package ru.tashkent.messenger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tashkent.domain.models.Message
import ru.tashkent.domain.repositories.MessageRepository

class ChatViewModel(
    private val chatId: String,
    private val repository: MessageRepository
) : ViewModel() {

    val newMessages = repository.messages

    private val messagesData = MutableSharedFlow<List<Message>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages = messagesData.asSharedFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            repository.getMessagesByChatId(chatId)
                .fold({ handleMessages(it) }, ::handleMessagesFailure)
        }
    }

    private fun handleMessages(messages: List<Message>) {
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

    class ViewModelFactory @AssistedInject constructor(
        @Assisted("chatId") private val chatId: String,
        private val repository: MessageRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == ChatViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(chatId, repository) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("chatId") chatId: String): ViewModelFactory
        }
    }
}
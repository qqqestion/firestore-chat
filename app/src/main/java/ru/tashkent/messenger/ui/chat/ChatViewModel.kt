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
import ru.tashkent.domain.fold
import ru.tashkent.domain.models.Message
import ru.tashkent.domain.usecases.GetMessagesUseCase
import ru.tashkent.domain.usecases.GetNewMessagesUseCase
import ru.tashkent.domain.usecases.SendMessageUseCase

class ChatViewModel(
    private val chatId: String,
    private val getMessages: GetMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val getNewMessages: GetNewMessagesUseCase
) : ViewModel() {

    val newMessages = getNewMessages.newMessages

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
            getMessages.invoke(GetMessagesUseCase.Params(chatId))
                .fold(::handleMessagesFailure, ::handleMessages)
        }
    }

    private fun handleMessages(messages: List<Message>) {
        messagesData.tryEmit(messages)
        val message = messages.lastOrNull()
        getNewMessages.doWork(chatId, message?.timeSent ?: 0L)
    }

    private fun handleMessagesFailure(throwable: Throwable) {
        TODO("Handling error in getting messages")
    }

    fun sendMessage(chatId: String, messageText: String) {
        viewModelScope.launch {
            sendMessage.invoke(SendMessageUseCase.Params(chatId, messageText))
        }
    }

    class ViewModelFactory @AssistedInject constructor(
        @Assisted("chatId") private val chatId: String,
        private val getMessages: GetMessagesUseCase,
        private val sendMessage: SendMessageUseCase,
        private val getNewMessages: GetNewMessagesUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == ChatViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(chatId, getMessages, sendMessage, getNewMessages) as T
        }

        @AssistedFactory
        interface Factory {

            fun create(@Assisted("chatId") chatId: String): ViewModelFactory
        }
    }
}
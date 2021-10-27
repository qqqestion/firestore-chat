package ru.tashkent.messenger.ui.mychats

import ru.tashkent.domain.models.Chat

sealed class MyChatsState {

    object Loading : MyChatsState()

    data class Success(val chats: List<Chat>) : MyChatsState()

    data class Failure(val failure: Throwable) : MyChatsState()
}

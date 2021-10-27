package ru.tashkent.notes.ui.mychats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import ru.tashkent.data.repositories.AuthRepository
import ru.tashkent.data.repositories.ChatRepository
import ru.tashkent.domain.models.Chat

class MyChatsViewModel : ViewModel() {

    private val repository = ChatRepository()
    private val auth = AuthRepository()

    private val stateData: MutableStateFlow<MyChatsState> = MutableStateFlow(MyChatsState.Loading)
    val state = stateData.asStateFlow()


    init {
        refreshChats()
        viewModelScope.launch {
            auth.signIn().onFailure {
                logcat { it.asLog() }
            }
        }
    }

    fun refreshChats() {
        viewModelScope.launch {
            stateData.tryEmit(MyChatsState.Loading)
            repository.getChats().fold(::handleSuccess, ::handleFailure)
        }
    }

    private fun handleSuccess(chats: List<Chat>) {
        stateData.tryEmit(MyChatsState.Success(chats))
    }

    private fun handleFailure(throwable: Throwable) {
        logcat { throwable.asLog() }
        stateData.tryEmit(MyChatsState.Failure(throwable))
    }
}
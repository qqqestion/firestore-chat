package ru.tashkent.messenger.ui.mychats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import ru.tashkent.domain.models.Chat
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.domain.repositories.ChatRepository
import javax.inject.Inject

class MyChatsViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val stateData: MutableStateFlow<MyChatsState> = MutableStateFlow(MyChatsState.Loading)
    val state = stateData.asStateFlow()

    init {
        refreshChats()
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

    class ViewModelFactory @Inject constructor(
        private val repository: ChatRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == MyChatsViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return MyChatsViewModel(repository) as T
        }
    }
}
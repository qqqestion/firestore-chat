package ru.tashkent.messenger.ui.mychats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import logcat.asLog
import logcat.logcat
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.fold
import ru.tashkent.domain.models.Chat
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.domain.repositories.ChatRepository
import ru.tashkent.domain.usecases.GetChatsUseCase
import javax.inject.Inject

class MyChatsViewModel(
    private val getChats: GetChatsUseCase
) : ViewModel() {

    private val stateData: MutableStateFlow<MyChatsState> = MutableStateFlow(MyChatsState.Loading)
    val state = stateData.asStateFlow()

    init {
        refreshChats()
    }

    fun refreshChats() {
        viewModelScope.launch {
            stateData.tryEmit(MyChatsState.Loading)
            getChats.invoke(ResultUseCase.NoneParams).fold(::handleFailure, ::handleSuccess)
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
        private val getChats: GetChatsUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == MyChatsViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return MyChatsViewModel(getChats) as T
        }
    }
}
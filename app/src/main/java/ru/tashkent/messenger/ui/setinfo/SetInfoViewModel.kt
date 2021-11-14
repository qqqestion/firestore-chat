package ru.tashkent.messenger.ui.setinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tashkent.domain.Either
import ru.tashkent.domain.usecases.SetInfoUseCase
import ru.tashkent.domain.models.User
import javax.inject.Inject

class SetInfoViewModel(
    private val setInfo: SetInfoUseCase
) : ViewModel() {

    enum class State {
        InputError,
        Empty,
        Loading,
        Success
    }

    private val stateData = MutableStateFlow(State.Empty)
    val state = stateData.asStateFlow()

    fun submit(name: String) {
        val validatedName = User.Name.createIfValid(name)
        when {
            validatedName == null -> stateData.tryEmit(State.InputError)
            else -> saveName(validatedName)
        }
    }

    private fun saveName(name: User.Name) {
        stateData.tryEmit(State.Loading)
        viewModelScope.launch {
            when (setInfo.doWork(name)) {
                is Either.Left -> TODO()
                is Either.Right -> stateData.tryEmit(State.Success)
            }
        }
    }

    class Factory @Inject constructor(
        private val setInfo: SetInfoUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SetInfoViewModel::class.java)
            return SetInfoViewModel(setInfo) as T
        }
    }
}
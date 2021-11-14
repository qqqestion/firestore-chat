package ru.tashkent.messenger.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tashkent.domain.AuthUseCase
import ru.tashkent.domain.models.User
import javax.inject.Inject

class LoginViewModel(
    private val auth: AuthUseCase
) : ViewModel() {

    sealed class Effect {
        object NavigateToSetupAccount : Effect()
        object NavigateToMain : Effect()
        data class Error(val error: AuthUseCase.AuthResult.Error) : Effect()
    }

    private val effectsData = MutableSharedFlow<Effect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effects: SharedFlow<Effect> = effectsData.asSharedFlow()

    private val stateData: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Empty)
    val state: StateFlow<AuthState> = stateData.asStateFlow()

    fun login(email: String, password: String) {
        val validatedEmail = User.Email.createIfValid(email)
        val validatedPassword = User.Password.createIfValid(password)
        when {
            validatedEmail == null -> stateData.tryEmit(AuthState.InputError.EmailError)
            validatedPassword == null -> stateData.tryEmit(AuthState.InputError.PasswordError)
            else -> submitLogin(validatedEmail, validatedPassword)
        }
    }

    private fun submitLogin(email: User.Email, password: User.Password) {
        stateData.tryEmit(AuthState.Loading)
        viewModelScope.launch {
            handleAuth(auth.doWork(email, password))
        }
    }

    private fun handleAuth(response: AuthUseCase.AuthResult) {
        val action = when (response) {
            AuthUseCase.AuthResult.UserLogin -> Effect.NavigateToMain
            AuthUseCase.AuthResult.UserCreated, AuthUseCase.AuthResult.UserWithoutName -> Effect.NavigateToSetupAccount
            is AuthUseCase.AuthResult.Error -> Effect.Error(response)
        }
        stateData.tryEmit(AuthState.Done)
        effectsData.tryEmit(action)
    }

    class Factory @Inject constructor(
        private val authUseCase: AuthUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == LoginViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authUseCase) as T
        }
    }
}
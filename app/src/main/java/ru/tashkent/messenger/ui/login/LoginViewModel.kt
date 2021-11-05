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

    private val stateData: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Empty)
    val state: StateFlow<AuthState> = stateData.asStateFlow()

    enum class Effect {
        NavigateToSetupAccount,
        NavigateToMain
    }

    private val effectsData = MutableSharedFlow<Effect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effects: SharedFlow<Effect> = effectsData.asSharedFlow()

    init {
        viewModelScope.launch {
            auth.flow.collect(::handleAuth)
        }
    }

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
            auth.auth(email, password)
        }
    }

    private fun handleAuth(response: AuthUseCase.AuthResult) {
        val action = when (response) {
            AuthUseCase.AuthResult.UserLogin -> Effect.NavigateToMain
            AuthUseCase.AuthResult.UserCreated, AuthUseCase.AuthResult.UserWithoutName -> Effect.NavigateToSetupAccount
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
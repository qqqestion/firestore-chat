package ru.tashkent.messenger.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.tashkent.domain.AuthUseCase
import ru.tashkent.domain.models.User
import javax.inject.Inject

class LoginViewModel(
    private val auth: AuthUseCase
) : ViewModel() {

    private val stateData: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Empty)
    val state: StateFlow<AuthState> = stateData.asStateFlow()

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
            AuthUseCase.AuthResult.UserLogin -> AuthState.LoginSuccess
            AuthUseCase.AuthResult.UserCreated, AuthUseCase.AuthResult.UserWithoutName -> AuthState.NavigateToSetupAccount
        }
        stateData.tryEmit(action)
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
package ru.tashkent.messenger.ui.login

sealed class AuthState {

    object Empty : AuthState()
    object Loading : AuthState()
    object LoginSuccess : AuthState()
    object NavigateToSetupAccount : AuthState()

    class Error(val error: Throwable) : AuthState()

    sealed class InputError : AuthState() {
        object EmailError : InputError()
        object PasswordError : InputError()
    }
}
package ru.tashkent.messenger.ui.login

sealed class AuthState {

    object Empty : AuthState()
    object Loading : AuthState()
    object Done : AuthState()

    sealed class InputError : AuthState() {
        object EmailError : InputError()
        object PasswordError : InputError()
    }
}

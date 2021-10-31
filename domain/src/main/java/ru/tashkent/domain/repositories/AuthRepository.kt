package ru.tashkent.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.tashkent.domain.VoidResult

interface AuthRepository {

    suspend fun signIn(): VoidResult

    sealed class AuthState {
        object NotAuthorized : AuthState()
        class Authorized(val userId: String) : AuthState()
    }

    val flow: Flow<AuthState>
}
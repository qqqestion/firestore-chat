package ru.tashkent.data.repositories

import com.google.firebase.auth.FirebaseAuth
import ru.tashkent.data.awaitResult
import ru.tashkent.domain.AuthRepository
import ru.tashkent.domain.VoidResult

internal class MessengerAuthRepository : AuthRepository {

    override suspend fun signIn(): VoidResult {
        if (FirebaseAuth.getInstance().uid == null) {
            return FirebaseAuth.getInstance().signInAnonymously().awaitResult().map { /* no-op */ }
        }
        return VoidResult.success(Unit)
    }
}

fun AuthRepository(): AuthRepository = MessengerAuthRepository()

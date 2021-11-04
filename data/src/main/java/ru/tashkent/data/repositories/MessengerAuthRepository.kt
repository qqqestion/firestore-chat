package ru.tashkent.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.tashkent.data.awaitResult
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.domain.VoidResult
import javax.inject.Inject

internal class MessengerAuthRepository @Inject constructor(): AuthRepository {

    private val authStateData = MutableSharedFlow<AuthRepository.AuthState>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var authListener: (FirebaseAuth) -> Unit = {
        authStateData.tryEmit(
            if (it.currentUser == null) AuthRepository.AuthState.NotAuthorized
            else AuthRepository.AuthState.Authorized(
                it.uid!!
            )
        )
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override val flow: Flow<AuthRepository.AuthState>
        get() = authStateData.asSharedFlow()

    override suspend fun signIn(): VoidResult {
        if (FirebaseAuth.getInstance().uid == null) {
            return FirebaseAuth.getInstance().signInAnonymously().awaitResult().map { /* no-op */ }
        }
        FirebaseAuth.getInstance().addAuthStateListener {
            it.currentUser != null
        }
        return VoidResult.success(Unit)
    }
}

fun AuthRepository(): AuthRepository = MessengerAuthRepository()

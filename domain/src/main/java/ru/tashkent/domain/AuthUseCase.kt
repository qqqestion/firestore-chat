package ru.tashkent.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    sealed class AuthResult {

        object UserCreated : AuthResult()

        object UserLogin : AuthResult()

        object UserWithoutName : AuthResult()
    }

    private val flowData = MutableSharedFlow<AuthResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val flow: SharedFlow<AuthResult> = flowData.asSharedFlow()

    suspend fun auth(email: User.Email, password: User.Password) {
        val create = authRepository.createAccount(email, password)
        when (create) {
            is Either.Left -> {
                when (create.value) {
                    AuthRepository.RegistrationError.Unknown -> TODO()
                    AuthRepository.RegistrationError.UserExists -> {
                        val login = authRepository.login(email, password)
                        when (login) {
                            is Either.Left -> TODO()
                            is Either.Right -> {
                                flowData.tryEmit(
                                    if (authRepository.checkIfAdditionalInfoSet()) AuthResult.UserLogin
                                    else AuthResult.UserWithoutName
                                )
                            }
                        }
                    }
                }
            }
            is Either.Right -> flowData.tryEmit(AuthResult.UserCreated)
        }
    }
}
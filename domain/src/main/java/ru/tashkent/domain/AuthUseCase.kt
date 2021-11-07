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

        sealed class Error : AuthResult() {

            object InvalidPassword : Error()

            object Unknown : Error()
        }
    }

    private val flowData = MutableSharedFlow<AuthResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val flow: SharedFlow<AuthResult> = flowData.asSharedFlow()

    suspend fun auth(email: User.Email, password: User.Password) {
        when (val create = authRepository.createAccount(email, password)) {
            is Either.Left -> {
                handleRegistrationError(create.value, email, password)
            }
            is Either.Right -> flowData.tryEmit(AuthResult.UserCreated)
        }
    }

    private suspend fun handleRegistrationError(
        value: AuthRepository.RegistrationError,
        email: User.Email,
        password: User.Password
    ) {
        when (value) {
            AuthRepository.RegistrationError.Unknown -> flowData.tryEmit(AuthResult.Error.Unknown)
            AuthRepository.RegistrationError.UserExists -> {
                when (val response = authRepository.login(email, password)) {
                    is Either.Left -> {
                        when (response.value) {
                            AuthRepository.LoginError.InvalidPassword -> flowData.tryEmit(AuthResult.Error.InvalidPassword)
                            AuthRepository.LoginError.Unknown -> flowData.tryEmit(AuthResult.Error.Unknown)
                        }
                    }
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
}
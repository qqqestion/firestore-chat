package ru.tashkent.domain.usecases

import ru.tashkent.domain.Either
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

    suspend fun doWork(email: User.Email, password: User.Password): AuthResult =
        when (val create = authRepository.createAccount(email, password)) {
            is Either.Left -> {
                handleRegistrationError(create.value, email, password)
            }
            is Either.Right -> AuthResult.UserCreated
        }


    private suspend fun handleRegistrationError(
        value: AuthRepository.RegistrationError,
        email: User.Email,
        password: User.Password
    ): AuthResult = when (value) {
        AuthRepository.RegistrationError.Unknown -> AuthResult.Error.Unknown
        AuthRepository.RegistrationError.UserExists -> {
            when (val response = authRepository.login(email, password)) {
                is Either.Left -> {
                    when (response.value) {
                        AuthRepository.LoginError.InvalidPassword -> AuthResult.Error.InvalidPassword
                        AuthRepository.LoginError.Unknown -> AuthResult.Error.Unknown
                    }
                }
                is Either.Right -> {
                    if (authRepository.checkIfAdditionalInfoSet()) AuthResult.UserLogin
                    else AuthResult.UserWithoutName
                }
            }
        }
    }
}
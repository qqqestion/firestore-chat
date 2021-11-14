package ru.tashkent.domain.repositories

import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.models.User

interface AuthRepository {

    suspend fun isAuthorized(): Boolean

    suspend fun createAccount(
        email: User.Email,
        password: User.Password
    ): EmptyEither<RegistrationError>

    suspend fun login(
        email: User.Email,
        password: User.Password
    ): EmptyEither<LoginError>

    suspend fun checkIfAdditionalInfoSet(): Boolean

    sealed class LoginError {
        object Unknown : LoginError()

        object InvalidPassword : LoginError()
    }

    sealed class RegistrationError {
        object UserExists : RegistrationError()
        object Unknown : RegistrationError()
    }
}
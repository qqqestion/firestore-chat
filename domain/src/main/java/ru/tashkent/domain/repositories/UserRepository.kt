package ru.tashkent.domain.repositories

import ru.tashkent.domain.Either
import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.models.User

interface UserRepository {

    suspend fun saveUser(user: User): EmptyEither<Throwable>

    suspend fun getUser(): Either<Throwable, User>

    suspend fun getUserById(userId: String): Either<Throwable, User?>
}
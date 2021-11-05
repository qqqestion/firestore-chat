package ru.tashkent.domain

import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.UserRepository
import java.lang.Exception
import javax.inject.Inject

class SetInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun doWork(name: User.Name): EmptyEither<Throwable> = try {
        userRepository.saveUser(User(name))
        Either.Right(Unit)
    } catch (e: Exception) {
        Either.Left(e)
    }
}
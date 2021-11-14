package ru.tashkent.domain.usecases

import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.UserRepository
import javax.inject.Inject

class SetInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun doWork(name: User.Name): EmptyEither<Throwable> =
        userRepository.saveUser(User(name))
}
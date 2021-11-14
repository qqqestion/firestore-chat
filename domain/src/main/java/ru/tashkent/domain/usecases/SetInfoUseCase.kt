package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.models.User
import ru.tashkent.domain.repositories.UserRepository
import javax.inject.Inject

class SetInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<SetInfoUseCase.Params, EmptyEither<Throwable>>(dispatchers) {

    override suspend fun doWork(params: Params): EmptyEither<Throwable> =
        userRepository.saveUser(User(params.name))

    data class Params(val name: User.Name)
}
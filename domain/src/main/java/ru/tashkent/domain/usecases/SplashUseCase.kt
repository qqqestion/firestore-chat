package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.repositories.AuthRepository
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<ResultUseCase.NoneParams, Boolean>(dispatchers) {

    override suspend fun doWork(params: NoneParams): Boolean = authRepository.isAuthorized()
}
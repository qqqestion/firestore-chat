package ru.tashkent.domain

import kotlinx.coroutines.withContext

abstract class ResultUseCase<in T, out R>(
    private val dispatchers: CoroutineDispatchers
) {

    protected abstract suspend fun doWork(params: T): R

    suspend operator fun invoke(params: T) = withContext(dispatchers.io) { doWork(params) }

    object NoneParams
}
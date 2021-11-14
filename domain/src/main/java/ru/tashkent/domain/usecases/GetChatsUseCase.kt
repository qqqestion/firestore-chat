package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.Either
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.models.Chat
import ru.tashkent.domain.repositories.ChatRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<ResultUseCase.NoneParams, Either<Throwable, List<Chat>>>(dispatchers) {

    override suspend fun doWork(params: NoneParams): Either<Throwable, List<Chat>> =
        chatRepository.getChats()
}
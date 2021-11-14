package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.Either
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.models.Message
import ru.tashkent.domain.repositories.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<GetMessagesUseCase.Params, Either<Throwable, List<Message>>>(dispatchers) {

    override suspend fun doWork(params: Params): Either<Throwable, List<Message>> =
        messageRepository.getMessagesByChatId(params.chatId)

    data class Params(val chatId: String)
}
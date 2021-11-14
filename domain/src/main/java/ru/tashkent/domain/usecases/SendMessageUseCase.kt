package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.EmptyEither
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.repositories.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<SendMessageUseCase.Params, Unit>(dispatchers) {

    override suspend fun doWork(params: Params): Unit =
        messageRepository.sendMessage(params.chatId, params.text)

    data class Params(val chatId: String, val text: String)
}
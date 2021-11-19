package ru.tashkent.domain.usecases

import ru.tashkent.domain.CoroutineDispatchers
import ru.tashkent.domain.Either
import ru.tashkent.domain.ResultUseCase
import ru.tashkent.domain.mapRight
import ru.tashkent.domain.models.Message
import ru.tashkent.domain.repositories.MessageRepository
import ru.tashkent.domain.repositories.UserRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    dispatchers: CoroutineDispatchers
) : ResultUseCase<GetMessagesUseCase.Params, Either<Throwable, List<Message>>>(dispatchers) {

    override suspend fun doWork(params: Params): Either<Throwable, List<Message>> =
        messageRepository.getMessagesByChatId(params.chatId)
            .mapRight { messages ->
                messages.mapNotNull { message ->
                    when (val user = userRepository.getUserById(message.senderId)) {
                        is Either.Left -> null
                        is Either.Right -> message.copy(senderName = user.value?.name?.value ?: "")
                    }
                }
            }

    data class Params(val chatId: String)
}
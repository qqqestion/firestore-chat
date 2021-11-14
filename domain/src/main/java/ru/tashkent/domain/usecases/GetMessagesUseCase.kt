package ru.tashkent.domain.usecases

import ru.tashkent.domain.repositories.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {

    suspend fun doWork(chatId: String) = messageRepository.getMessagesByChatId(chatId)
}
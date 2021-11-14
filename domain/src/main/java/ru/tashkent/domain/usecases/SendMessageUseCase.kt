package ru.tashkent.domain.usecases

import ru.tashkent.domain.repositories.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {

    suspend fun doWork(chatId: String, text: String) = messageRepository.sendMessage(chatId, text)
}
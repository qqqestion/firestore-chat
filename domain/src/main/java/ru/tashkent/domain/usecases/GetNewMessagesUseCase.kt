package ru.tashkent.domain.usecases

import ru.tashkent.domain.repositories.MessageRepository
import javax.inject.Inject

class GetNewMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {

    val newMessages = messageRepository.messages

    fun doWork(chatId: String, lastTimeSent: Long) =
        messageRepository.initMessages(chatId, lastTimeSent)
}
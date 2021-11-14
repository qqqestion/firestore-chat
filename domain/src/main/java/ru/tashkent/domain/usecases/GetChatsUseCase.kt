package ru.tashkent.domain.usecases

import ru.tashkent.domain.repositories.ChatRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend fun doWork() = chatRepository.getChats()
}
package ru.tashkent.domain.repositories

import ru.tashkent.domain.models.Chat

interface ChatRepository {

    suspend fun getChats(): Result<List<Chat>>
}
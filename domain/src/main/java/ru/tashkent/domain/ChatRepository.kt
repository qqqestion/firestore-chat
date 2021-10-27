package ru.tashkent.domain

import ru.tashkent.domain.models.Chat

interface ChatRepository {

    suspend fun getChats(): Result<List<Chat>>
}
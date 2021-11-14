package ru.tashkent.domain.repositories

import ru.tashkent.domain.Either
import ru.tashkent.domain.models.Chat

interface ChatRepository {

    suspend fun getChats(): Either<Throwable, List<Chat>>
}
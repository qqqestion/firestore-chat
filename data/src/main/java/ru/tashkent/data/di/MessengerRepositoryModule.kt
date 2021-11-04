package ru.tashkent.data.di

import dagger.Module
import dagger.Provides
import ru.tashkent.data.repositories.AuthRepository
import ru.tashkent.data.repositories.ChatRepository
import ru.tashkent.data.repositories.MessageRepository
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.domain.repositories.ChatRepository
import ru.tashkent.domain.repositories.MessageRepository

@Module
class MessengerRepositoryModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepository()

    @Provides
    fun provideChatRepository(): ChatRepository = ChatRepository()

    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepository()
}
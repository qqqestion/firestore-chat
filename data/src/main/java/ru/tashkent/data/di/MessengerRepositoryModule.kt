package ru.tashkent.data.di

import dagger.Module
import dagger.Provides
import ru.tashkent.data.repositories.MessengerAuthRepository
import ru.tashkent.data.repositories.MessengerChatRepository
import ru.tashkent.data.repositories.MessengerMessageRepository
import ru.tashkent.data.repositories.MessengerUserRepository
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.domain.repositories.ChatRepository
import ru.tashkent.domain.repositories.MessageRepository
import ru.tashkent.domain.repositories.UserRepository

@Module
class MessengerRepositoryModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = MessengerAuthRepository()

    @Provides
    fun provideChatRepository(): ChatRepository = MessengerChatRepository()

    @Provides
    fun provideMessageRepository(): MessageRepository = MessengerMessageRepository()

    @Provides
    fun provideUserRepository(): UserRepository = MessengerUserRepository()
}
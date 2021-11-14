package ru.tashkent.messenger.di

import dagger.Module
import ru.tashkent.data.di.MessengerRepositoryModule

@Module(includes = [MessengerRepositoryModule::class, CoroutineDispatchersModule::class])
class MainModule
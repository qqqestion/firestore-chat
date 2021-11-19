package ru.tashkent.messenger.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import ru.tashkent.domain.CoroutineDispatchers

@Module
class CoroutineDispatchersModule {

    @Provides
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )
}
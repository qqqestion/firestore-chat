package ru.tashkent.messenger.di

import dagger.Provides
import kotlinx.coroutines.Dispatchers
import ru.tashkent.domain.CoroutineDispatchers

class CoroutineDispatchersModule {

    @Provides
    fun provideCoroutineDispatchers() = CoroutineDispatchers(
        Dispatchers.IO,
        Dispatchers.Default,
        Dispatchers.Main
    )
}
package ru.tashkent.messenger.di

import dagger.Component
import dagger.Module
import ru.tashkent.data.di.MessengerRepositoryModule
import ru.tashkent.domain.repositories.AuthRepository
import ru.tashkent.messenger.ui.chat.ChatFragment
import ru.tashkent.messenger.ui.mychats.MyChatsFragment

@Component(modules = [MessengerRepositoryModule::class])
interface AppComponent {

    fun inject(fragment: ChatFragment)
    fun inject(fragment: MyChatsFragment)
}
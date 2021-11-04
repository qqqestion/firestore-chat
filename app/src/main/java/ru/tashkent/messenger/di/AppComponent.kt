package ru.tashkent.messenger.di

import dagger.Component
import ru.tashkent.messenger.ui.chat.ChatFragment
import ru.tashkent.messenger.ui.mychats.MyChatsFragment

@Component(modules = [MainModule::class])
interface AppComponent {

    fun inject(fragment: ChatFragment)
    fun inject(fragment: MyChatsFragment)
}
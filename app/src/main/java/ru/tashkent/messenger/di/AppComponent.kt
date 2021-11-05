package ru.tashkent.messenger.di

import dagger.Component
import ru.tashkent.messenger.ui.chat.ChatFragment
import ru.tashkent.messenger.ui.login.LoginFragment
import ru.tashkent.messenger.ui.mychats.MyChatsFragment
import ru.tashkent.messenger.ui.setinfo.SetInfoFragment

@Component(modules = [MainModule::class])
interface AppComponent {

    fun inject(fragment: ChatFragment)
    fun inject(fragment: MyChatsFragment)
    fun inject(fragment: LoginFragment)
    fun inject(setInfoFragment: SetInfoFragment)
}
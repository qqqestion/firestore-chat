package ru.tashkent.messenger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.tashkent.messenger.ui.chat.ChatFragment
import javax.inject.Inject


class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
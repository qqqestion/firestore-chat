package ru.tashkent.messenger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.tashkent.messenger.di.AppComponent
import ru.tashkent.messenger.di.DaggerAppComponent
import ru.tashkent.messenger.ui.chat.ChatFragment
import timber.log.Timber
import javax.inject.Inject


class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
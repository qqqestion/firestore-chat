package ru.tashkent.messenger.exts

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import ru.tashkent.messenger.App
import ru.tashkent.messenger.di.AppComponent


val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> this.appComponent
        else -> this.applicationContext.appComponent
    }

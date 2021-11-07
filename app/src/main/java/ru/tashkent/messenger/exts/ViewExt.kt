package ru.tashkent.messenger.exts

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(@StringRes messageResId: Int, duration: Int = Snackbar.LENGTH_SHORT) =
    Snackbar.make(this, messageResId, duration).show()

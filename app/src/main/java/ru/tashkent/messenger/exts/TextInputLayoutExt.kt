package ru.tashkent.messenger.exts

import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.textOrEmpty() = editText?.text?.toString()?.trim().orEmpty()

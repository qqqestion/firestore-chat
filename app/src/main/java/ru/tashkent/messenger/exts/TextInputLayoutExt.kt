package ru.tashkent.messenger.exts

import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.textOrEmpty() = editText?.text?.toString()?.trim().orEmpty()

fun TextInputLayout.showErrorWithEnable(errorMessage: String) {
    this.error = errorMessage
    this.isErrorEnabled = true
}

fun TextInputLayout.showErrorResId(@StringRes stringResId: Int) =
    showErrorWithEnable(context.getString(stringResId))

fun TextInputLayout.clearError() {
    this.error = null
    this.isErrorEnabled = false
}

fun TextInputLayout.clearErrorOnAnyInput() =
    this.editText?.doAfterTextChanged { clearError() }

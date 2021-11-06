package ru.tashkent.data.exts

import com.google.firebase.Timestamp

internal val Timestamp.millis
    get() = this.seconds * 1000 + this.nanoseconds / 1000000

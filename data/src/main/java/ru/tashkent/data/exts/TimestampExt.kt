package ru.tashkent.data

import com.google.firebase.Timestamp

val Timestamp.millis
    get() = this.seconds * 1000 + this.nanoseconds / 1000000

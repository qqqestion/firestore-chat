package ru.tashkent.data

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await


suspend fun <T> Task<T>.awaitResult(): Result<T> {
    return try {
        Result.success(await())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

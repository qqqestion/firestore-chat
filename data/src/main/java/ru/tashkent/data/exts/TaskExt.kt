package ru.tashkent.data

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import ru.tashkent.domain.Either


internal suspend fun <T> Task<T>.awaitResult(): Result<T> {
    return try {
        Result.success(await())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

internal suspend fun <T> Task<T>.awaitTask(): Either<Throwable, T> {
    return try {
        Either.Right(await())
    } catch (e: Exception) {
        Either.Left(e)
    }
}

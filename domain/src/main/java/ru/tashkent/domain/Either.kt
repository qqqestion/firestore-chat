package ru.tashkent.domain

import java.lang.Exception

sealed class Either<out A, out B> {

    data class Left<out A>(val value: A) : Either<A, Nothing>()
    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

inline fun <A, B, C> Either<A, B>.mapLeft(fn: (A) -> C): Either<C, B> = when (this) {
    is Either.Left -> Either.Left(fn(value))
    is Either.Right -> this
}

inline fun <A, B, C> Either<A, B>.mapRight(fn: (B) -> C): Either<A, C> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(fn(value))
}

inline fun <A, B> Either<A, B>.fold(fnL: (A) -> Unit, fnR: (B) -> Unit): Either<A, B> = this.apply {
    when (this) {
        is Either.Left -> fnL(value)
        is Either.Right -> fnR(value)
    }
}

inline fun <B> runEither(body: () -> B): Either<Throwable, B> = try {
    Either.Right(body())
} catch (e: Throwable) {
    Either.Left(e)
}

typealias EmptyEither<E> = Either<E, Unit>

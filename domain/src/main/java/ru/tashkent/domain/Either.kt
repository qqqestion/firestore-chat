package ru.tashkent.domain

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

typealias EmptyEither<E> = Either<E, Unit>

package ru.tashkent.domain

interface AuthRepository {

    suspend fun signIn(): VoidResult
}
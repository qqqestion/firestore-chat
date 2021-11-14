package ru.tashkent.domain.repositories

import ru.tashkent.domain.models.User

interface UserRepository {

    suspend fun saveUser(user: User)

    suspend fun getUser(): User

    suspend fun getUserById(userId: String): User?
}
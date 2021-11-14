package ru.tashkent.domain

import ru.tashkent.domain.repositories.AuthRepository
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun isAuthorized() = authRepository.isAuthorized()
}
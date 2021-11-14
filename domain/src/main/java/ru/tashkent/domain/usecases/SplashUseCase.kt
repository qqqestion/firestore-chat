package ru.tashkent.domain.usecases

import ru.tashkent.domain.repositories.AuthRepository
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun isAuthorized() = authRepository.isAuthorized()
}
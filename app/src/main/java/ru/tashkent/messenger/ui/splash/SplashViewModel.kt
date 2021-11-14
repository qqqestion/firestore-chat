package ru.tashkent.messenger.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.flow
import ru.tashkent.domain.SplashUseCase
import javax.inject.Inject

class SplashViewModel(
    private val splashUseCase: SplashUseCase
) : ViewModel() {

    val authorizedStatus = flow { emit(splashUseCase.isAuthorized()) }

    class Factory @Inject constructor(
        private val useCase: SplashUseCase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == SplashViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(useCase) as T
        }
    }
}
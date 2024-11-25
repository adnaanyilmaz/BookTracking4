package com.example.booktracking4.presentation.fragments.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import com.example.booktracking4.presentation.fragments.splash.SplashContract.SplashUiEffect
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _splashUiEffect by lazy { Channel<SplashUiEffect>() }
    val splashUiEffect: Flow<SplashUiEffect> by lazy { _splashUiEffect.receiveAsFlow() }

    init {
        isLoggedIn()
    }

    private fun isLoggedIn() = viewModelScope.launch{
        if (authRepository.isUserLoggedIn()) {
            emitUiEffect(SplashUiEffect.GoToMainScreen)
            emitUiEffect(SplashUiEffect.ShowToast("Logged In"))
        } else {
            emitUiEffect(SplashUiEffect.GoToSignInScreen)
            emitUiEffect(SplashUiEffect.ShowToast("Please Login or Register"))
        }
    }



    private suspend fun emitUiEffect(uiEffect: SplashUiEffect) {
        _splashUiEffect.send(uiEffect)
    }
}
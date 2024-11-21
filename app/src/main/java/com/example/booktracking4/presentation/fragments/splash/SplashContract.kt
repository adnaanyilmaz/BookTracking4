package com.example.booktracking4.presentation.fragments.splash

object SplashContract {

    sealed class SplashUiEffect {
        data class ShowToast(val message: String): SplashUiEffect()
        data object GoToMainScreen : SplashUiEffect()
        data object GoToSignInScreen : SplashUiEffect()
    }
}
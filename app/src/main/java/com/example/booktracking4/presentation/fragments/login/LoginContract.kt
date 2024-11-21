package com.example.booktracking4.presentation.fragments.login

object LoginContract {

    data class LoginUiState(
        val isLoading: Boolean = false
    )

    sealed class LoginUiEffect {
        data class ShowToast(val message: String): LoginUiEffect()
        data object GoToHomeScreen : LoginUiEffect()
    }
}
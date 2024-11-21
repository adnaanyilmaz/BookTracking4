package com.example.booktracking4.presentation.fragments.register

object RegisterContract{
    data class  RegisterUiState(
        val isLoading: Boolean = false,
    )

    sealed class RegisterUiEffect {
        data class ShowToastMessage(val message: String): RegisterUiEffect()
        data object GoToSignInScreen: RegisterUiEffect()
    }
}


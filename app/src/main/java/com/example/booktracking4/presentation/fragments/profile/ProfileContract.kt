package com.example.booktracking4.presentation.fragments.profile

object ProfileContract {

    data class UiState(
        val isLoading: Boolean = false,
    )

    sealed class UiEffect {
        data object GoToLoginScreen : UiEffect()
        data class ShowToastMessage(val message: String): UiEffect()

    }
}
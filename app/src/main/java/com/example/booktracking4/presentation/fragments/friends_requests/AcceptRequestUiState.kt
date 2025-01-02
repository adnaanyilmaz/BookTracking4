package com.example.booktracking4.presentation.fragments.friends_requests

sealed class AcceptRequestUiState {
    object Idle : AcceptRequestUiState()
    object Loading : AcceptRequestUiState()
    data class Success(val message: String) : AcceptRequestUiState()
    data class Error(val message: String) : AcceptRequestUiState()
}
package com.example.booktracking4.presentation.fragments.friends_requests

sealed class RejectRequestsUiState {
    object Idle: RejectRequestsUiState()
    data class Success(val message: String): RejectRequestsUiState()
    data class Error(val message: String): RejectRequestsUiState()
    object Loading: RejectRequestsUiState()
}

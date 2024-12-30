package com.example.booktracking4.presentation.fragments.friends

sealed class AddFriendUiState {
    object Loading : AddFriendUiState()
    data class Success(val message: String) : AddFriendUiState()
    data class Error(val message: String) : AddFriendUiState()
    object Idle : AddFriendUiState()
}
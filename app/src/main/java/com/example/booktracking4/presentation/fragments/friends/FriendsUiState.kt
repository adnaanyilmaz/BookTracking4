package com.example.booktracking4.presentation.fragments.friends

import com.example.booktracking4.data.remote.user.Friends

sealed class FriendsUiState {
    data class Success(val requests: List<Friends>): FriendsUiState()
    data class Error(val message: String): FriendsUiState()
    object Loading: FriendsUiState()
    object Idle: FriendsUiState()
}
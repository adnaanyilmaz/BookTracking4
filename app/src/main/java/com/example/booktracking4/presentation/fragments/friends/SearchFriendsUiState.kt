package com.example.booktracking4.presentation.fragments.friends

import com.example.booktracking4.data.remote.user.User

sealed class SearchFriendsUiState {
    object Loading : SearchFriendsUiState()
    data class Success(val users: List<User>) : SearchFriendsUiState()
    data class Error(val message: String) : SearchFriendsUiState()
    object Idle : SearchFriendsUiState()
}

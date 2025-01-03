package com.example.booktracking4.presentation.fragments.friends_detail

import com.example.booktracking4.data.remote.user.Read

sealed class FriendsDetailUiState {
    data class Success(val requests: List<Read>): FriendsDetailUiState()
    data class Error(val message: String): FriendsDetailUiState()
    data class Loading(val isLoading: Boolean=true): FriendsDetailUiState()
    object Idle: FriendsDetailUiState()
}

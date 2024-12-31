package com.example.booktracking4.presentation.fragments.friends_requests

import com.example.booktracking4.data.remote.user.FriendRequest

sealed class FriendRequestsUiState {
    object Idle : FriendRequestsUiState()
    object Loading : FriendRequestsUiState()
    data class Success(val friendRequests: List<FriendRequest>) : FriendRequestsUiState()
    data class Error(val message: String) : FriendRequestsUiState()
}
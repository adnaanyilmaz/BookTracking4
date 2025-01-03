package com.example.booktracking4.presentation.fragments.friends_requests

import com.example.booktracking4.data.remote.user.FriendsRequest


data class FriendRequestsUiState(
    val isLoading: Boolean? = true,
    val friendRequestList: List<FriendsRequest> = emptyList(),
    val errorMessageFriendRequestList: String? = null,
)

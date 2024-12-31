package com.example.booktracking4.presentation.fragments.friends_requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsRequestsViewModel @Inject constructor(
    private val repository: AddFriendsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _friendRequestsState = MutableStateFlow<FriendRequestsUiState>(FriendRequestsUiState.Idle)
    val friendRequestsState: StateFlow<FriendRequestsUiState> get() = _friendRequestsState

    private val _acceptRequestState = MutableStateFlow<AcceptRequestUiState>(AcceptRequestUiState.Idle)
    val acceptRequestState: StateFlow<AcceptRequestUiState> get() = _acceptRequestState

    fun getFriendRequests() {
        viewModelScope.launch {
            _friendRequestsState.value = FriendRequestsUiState.Loading
            val currentUserId = auth.uid
            if (currentUserId == null) {
                _friendRequestsState.value = FriendRequestsUiState.Error("User not logged in.")
                return@launch
            }
            when (val result = repository.getFriendRequests(currentUserId)) {
                is Resource.Success -> {
                    _friendRequestsState.value = FriendRequestsUiState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _friendRequestsState.value = FriendRequestsUiState.Error(result.message ?: "An unknown error occurred.")
                }
                is Resource.Loading -> {
                    // Optionally handle additional loading logic
                }
            }
        }
    }

    fun acceptFriendRequest(senderUid: String) {
        viewModelScope.launch {
            _acceptRequestState.value = AcceptRequestUiState.Loading
            val currentUserId = auth.uid
            if (currentUserId == null) {
                _acceptRequestState.value = AcceptRequestUiState.Error("User not logged in.")
                return@launch
            }
            when (val result = repository.acceptFriendRequest(currentUserId, senderUid)) {
                is Resource.Success -> {
                    _acceptRequestState.value = AcceptRequestUiState.Success
                    getFriendRequests() // Refresh the friend requests
                }
                is Resource.Error -> {
                    _acceptRequestState.value = AcceptRequestUiState.Error(result.message ?: "Failed to accept friend request.")
                }
                is Resource.Loading -> {
                    // Optionally handle additional loading logic
                }
            }
        }
    }

    fun rejectFriendRequest(senderUid: String) {
        viewModelScope.launch {
            _acceptRequestState.value = AcceptRequestUiState.Loading
            val currentUserId = auth.uid
            if (currentUserId == null) {
                _acceptRequestState.value = AcceptRequestUiState.Error("User not logged in.")
                return@launch
            }
            when (val result = repository.rejectFriendRequest(currentUserId, senderUid)) {
                is Resource.Success -> {
                    _acceptRequestState.value = AcceptRequestUiState.Success
                    getFriendRequests() // Refresh the friend requests
                }
                is Resource.Error -> {
                    _acceptRequestState.value = AcceptRequestUiState.Error(result.message ?: "Failed to reject friend request.")
                }
                is Resource.Loading -> {
                    // Optionally handle additional loading logic
                }
            }
        }
    }

    fun resetFriendRequestsState() {
        _friendRequestsState.value = FriendRequestsUiState.Idle
    }

    fun resetAcceptRequestState() {
        _acceptRequestState.value = AcceptRequestUiState.Idle
    }
}


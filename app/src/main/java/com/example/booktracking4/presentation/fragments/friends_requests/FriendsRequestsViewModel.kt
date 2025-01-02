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
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _friendRequestsState = MutableStateFlow<FriendRequestsUiState>(FriendRequestsUiState.Idle)
    val friendRequestsState: StateFlow<FriendRequestsUiState> get() = _friendRequestsState

    private val _acceptRequestState = MutableStateFlow<AcceptRequestUiState>(AcceptRequestUiState.Idle)
    val acceptRequestState: StateFlow<AcceptRequestUiState> get() = _acceptRequestState

    private val _rejectRequestState = MutableStateFlow<RejectRequestsUiState>(RejectRequestsUiState.Idle)
    val rejectRequestState: StateFlow<RejectRequestsUiState> get() = _rejectRequestState


    fun acceptFriendRequest(senderUserName: String) = viewModelScope.launch {
        try {
            val result = repository.acceptFriendRequest(
                receiverUid = auth.currentUser?.uid!!,
                senderUserName = senderUserName
            )
            if (result.isSuccess) {
                _acceptRequestState.value = AcceptRequestUiState.Success("Friend request accepted")
            } else {
                _acceptRequestState.value =
                    AcceptRequestUiState.Error("Friend request could not be accepted")
            }
        }catch (e: Exception){
            _acceptRequestState.value= AcceptRequestUiState.Error(e.message ?: "An unknown error occurred.")
        }
    }

    fun getFriendsRequests() = viewModelScope.launch {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _friendRequestsState.value = FriendRequestsUiState.Error("User not authenticated.")
            return@launch
        }

        _friendRequestsState.value = FriendRequestsUiState.Loading // Yükleme durumu

        val result = repository.getFriendsRequests(uid)
        when (result) {
            is Resource.Success -> {
                _friendRequestsState.value = FriendRequestsUiState.Success(result.data ?: emptyList())
            }
            is Resource.Error -> {
                _friendRequestsState.value = FriendRequestsUiState.Error("Friend requests could not be received.")
            }
            is Resource.Loading -> {}
        }
    }


    fun rejectFriendRequest(senderUserName: String) = viewModelScope.launch() {
       try {
           val result=repository.rejectFriendRequest(receiverUid = auth.currentUser?.uid!!, senderUserName = senderUserName)
           if (result.isSuccess){
               _rejectRequestState.value= RejectRequestsUiState.Success("Friend request rejected.")
           }else{
               _rejectRequestState.value= RejectRequestsUiState.Error("An unknown error occurred. Try again.")
           }
       }catch (e: Exception){
           _rejectRequestState.value= RejectRequestsUiState.Error(e.message ?:"An unknown error occurred.")
       }
    }


    fun resetFriendRequestsState() {
        _friendRequestsState.value = FriendRequestsUiState.Idle
    }

    fun resetAcceptRequestState() {
        _acceptRequestState.value = AcceptRequestUiState.Idle
    }
}


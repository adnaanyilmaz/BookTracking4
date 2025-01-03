package com.example.booktracking4.presentation.fragments.friends_requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.presentation.fragments.friends_requests.AcceptRequestUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsRequestsViewModel @Inject constructor(
    private val repository: AddFriendsRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FriendRequestsUiState())
    val uiState: StateFlow<FriendRequestsUiState> = _uiState.asStateFlow()

    private val _acceptRequestState = MutableStateFlow<AcceptRequestUiState>(AcceptRequestUiState.Idle)
    val acceptRequestState: StateFlow<AcceptRequestUiState> get() = _acceptRequestState

    private val _rejectRequestState = MutableStateFlow<RejectRequestsUiState>(RejectRequestsUiState.Idle)
    val rejectRequestState: StateFlow<RejectRequestsUiState> get() = _rejectRequestState


    fun acceptFriendRequest(senderUserName: String) {
        viewModelScope.launch {
            val result=repository.acceptFriendRequest(
                receiverUid = auth.currentUser?.uid!!,
                senderUserName = senderUserName
            )
            when(result){
                is Resource.Error -> _acceptRequestState.value= AcceptRequestUiState.Error(result.data.toString())
                is Resource.Loading -> _acceptRequestState.value= AcceptRequestUiState.Loading
                is Resource.Success -> _acceptRequestState.value= AcceptRequestUiState.Success(result.data.toString())
            }
            getFriendRequest()
        }


    }

    fun rejectFriendRequest(senderUserName: String) {
        viewModelScope.launch {
            val result=repository.rejectFriendRequest(
                receiverUid = auth.currentUser?.uid!!,
                senderUserName = senderUserName
            )
            when(result){
                is Resource.Error -> _rejectRequestState.value= RejectRequestsUiState.Error(result.data.toString())
                is Resource.Loading -> _rejectRequestState.value= RejectRequestsUiState.Loading
                is Resource.Success -> _rejectRequestState.value= RejectRequestsUiState.Success(result.data.toString())
            }
            getFriendRequest()
        }
    }

    init {
        getFriendRequest()
    }


    fun getFriendRequest() = viewModelScope.launch {
        val result = repository.getFriendsRequests(uid = auth.currentUser?.uid.orEmpty())
        when (result) {
            is Resource.Success -> {
                updateUiState { copy(isLoading = false, friendRequestList = result.data.orEmpty()) }
            }

            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }

            is Resource.Error -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        errorMessageFriendRequestList = result.message
                    )
                }
            }
        }
    }


    private fun updateUiState(block: FriendRequestsUiState.() -> FriendRequestsUiState) {
        _uiState.update(block)
    }


}


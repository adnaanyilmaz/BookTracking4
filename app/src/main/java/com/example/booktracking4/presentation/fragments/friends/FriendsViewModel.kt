package com.example.booktracking4.presentation.fragments.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.presentation.fragments.friends_requests.FriendRequestsUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repository: AddFriendsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _friendsState = MutableStateFlow<FriendsUiState>(FriendsUiState.Idle)
    val friendsState: StateFlow<FriendsUiState> get() = _friendsState

    init {
        getFriendsList()
    }

    fun getFriendsList() = viewModelScope.launch {
        val result = repository.getFriendsList(uid = auth.currentUser?.uid.orEmpty())
        when (result) {
            is Resource.Error -> _friendsState.value= FriendsUiState.Error(result.message.toString())
            is Resource.Loading -> {}
            is Resource.Success -> _friendsState.value= FriendsUiState.Success(result.data ?:emptyList())
        }
    }

}
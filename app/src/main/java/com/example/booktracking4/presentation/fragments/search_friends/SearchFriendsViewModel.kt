package com.example.booktracking4.presentation.fragments.search_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.presentation.fragments.search_friends.AddFriendUiState.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFriendsViewModel @Inject constructor(
    private val repository: AddFriendsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchFriendsUiState>(SearchFriendsUiState.Idle)
    val searchState: StateFlow<SearchFriendsUiState> get() = _searchState

    private val _addFriendState = MutableStateFlow<AddFriendUiState>(AddFriendUiState.Idle)
    val addFriendState: StateFlow<AddFriendUiState> get() = _addFriendState

    fun searchFriendsByUsername(username: String) {
        viewModelScope.launch {
            _searchState.value = SearchFriendsUiState.Loading
            try {
                val result = repository.searchFriendByUsername(username)
                if (result.isSuccess) {
                    _searchState.value = SearchFriendsUiState.Success(result.getOrThrow())
                } else {
                    _searchState.value =
                        SearchFriendsUiState.Error("No users found with username $username.")
                }
            } catch (e: Exception) {
                _searchState.value =
                    SearchFriendsUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun sendFriendRequest(friendUsername: String) {
        viewModelScope.launch {
            _addFriendState.value = AddFriendUiState.Loading
            try {
                val result = repository.sendFriendRequest(auth.uid.toString(), friendUsername)
                when (result) {
                    is Resource.Success<*> -> {
                        _addFriendState.value = Success(result.data.toString())
                    }

                    is Resource.Error -> _addFriendState.value =
                        AddFriendUiState.Error(result.message ?: "Failed to send friend request.")

                    is Resource.Loading -> {}
                }

            } catch (e: Exception) {
                _addFriendState.value =
                    AddFriendUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun resetSearchState() {
        _searchState.value = SearchFriendsUiState.Idle
    }

    fun resetAddFriendState() {
        _addFriendState.value = AddFriendUiState.Idle
    }
}

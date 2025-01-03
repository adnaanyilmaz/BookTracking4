package com.example.booktracking4.presentation.fragments.search_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _isFriend = MutableStateFlow<Boolean?>(null)
    val isFriend: StateFlow<Boolean?> = _isFriend

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

    fun sendFriendRequest(receiverUserName: String) = viewModelScope.launch {
        try {
            val result = repository.sendFriendRequest(
                senderUid = auth.currentUser?.uid!!,
                receiverUserName = receiverUserName
            )
            if (result.isSuccess) {
                _addFriendState.value = AddFriendUiState.Success(result.getOrThrow())
            } else {
                _addFriendState.value = AddFriendUiState.Error("Friend request could not be sent.")
            }
        } catch (e: Exception) {
            _addFriendState.value =
                AddFriendUiState.Error(e.message ?: "An unknown error occurred.")
        }
    }
    suspend fun checkFriendsState(uid: String): Boolean{
        val result=repository.isUserInFriendsList(currentUserUid = auth.currentUser?.uid!!, friendUid = uid)
        return result
    }
}

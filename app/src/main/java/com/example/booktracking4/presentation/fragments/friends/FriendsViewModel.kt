package com.example.booktracking4.presentation.fragments.friends

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
class FriendsViewModel @Inject constructor(
    private val repository: AddFriendsRepository,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _friendsState= MutableStateFlow<FriendsUiState>(FriendsUiState.Idle)
    val friendsState: StateFlow<FriendsUiState> get() = _friendsState

    fun getFriendsList() =viewModelScope.launch{
        val result=repository.getFriendsList(auth.currentUser?.uid!!)
        when(result){
            is Resource.Error -> _friendsState.value= FriendsUiState.Error("Friends could not be received.")
            is Resource.Loading -> {}
            is Resource.Success -> _friendsState.value= FriendsUiState.Success(result.data ?: emptyList())
        }
    }
}
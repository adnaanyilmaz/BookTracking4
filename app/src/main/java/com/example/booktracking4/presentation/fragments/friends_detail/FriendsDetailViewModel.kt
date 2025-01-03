package com.example.booktracking4.presentation.fragments.friends_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsDetailViewModel @Inject constructor(
    private val repository: UserRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<FriendsDetailUiState>(FriendsDetailUiState.Idle)
    val uiState: StateFlow<FriendsDetailUiState> get() = _uiState



    fun getFriendsBook(uid: String)=viewModelScope.launch{
        val result = repository.getUserBooks(userId = uid)

        when(result){
            is Resource.Error -> _uiState.value= FriendsDetailUiState.Error(result.message.toString())
            is Resource.Loading -> _uiState.value= FriendsDetailUiState.Loading(isLoading = true)
            is Resource.Success -> _uiState.value= FriendsDetailUiState.Success(result.data?.read ?:emptyList())
        }
    }
}
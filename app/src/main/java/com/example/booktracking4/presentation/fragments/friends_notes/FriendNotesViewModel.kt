package com.example.booktracking4.presentation.fragments.friends_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.favorite_books.FavoriteUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendNotesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _uiState = MutableStateFlow(FriendNotesUiState())
    val uiState: StateFlow<FriendNotesUiState> = _uiState.asStateFlow()

    fun getFriendsNotes() = viewModelScope.launch {
        val result=userRepository.getFriendsPublicNotes(
            userId = auth.currentUser?.uid!!
        )
        when(result){
            is Resource.Error -> updateUiState { copy(
                isLoading = false,
                notes = emptyList(),
                error = result.message ?: "unknown error"
            ) }
            is Resource.Loading -> updateUiState {
                copy(
                    isLoading = true
                )
            }
            is Resource.Success -> updateUiState { copy(
                isLoading = false,
                notes = result.data ?: emptyList(),
                userName = userRepository.getUserName(auth.currentUser?.uid!!)
            ) }
        }
    }
    private fun updateUiState(block: FriendNotesUiState.() -> FriendNotesUiState) {
        _uiState.update(block)
    }
}
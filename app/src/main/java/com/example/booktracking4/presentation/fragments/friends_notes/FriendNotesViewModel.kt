package com.example.booktracking4.presentation.fragments.friends_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.UserRepository
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

    init {
        getFriendsNotes()
    }

    fun getFriendsNotes() = viewModelScope.launch {
        updateUiState { copy(isLoading = true, error = "", notesWithUsernames = emptyList()) }

        val result = userRepository.getFriendsPublicNotes(
            userId = auth.currentUser?.uid ?: return@launch
        )

        when (result) {
            is Resource.Error -> updateUiState {
                copy(
                    isLoading = false,
                    notesWithUsernames = emptyList(),
                    error = result.message ?: "Unknown error"
                )
            }
            is Resource.Success -> {
                val notesWithUsernames = result.data ?: emptyList()
                updateUiState {
                    copy(
                        isLoading = false,
                        notesWithUsernames = notesWithUsernames
                    )
                }
            }
            is Resource.Loading -> updateUiState {
                copy(isLoading = true)
            }
        }
    }

    private fun updateUiState(block: FriendNotesUiState.() -> FriendNotesUiState) {
        _uiState.update(block)
    }
}

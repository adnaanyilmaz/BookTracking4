package com.example.booktracking4.presentation.fragments.admin_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.model.retrofit.Book
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.NotesRepository
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
class AdminNotesViewModel @Inject constructor(
    private val userRepository: UserRepository,

    ): ViewModel(){
    private var _uiState = MutableStateFlow(AdminNotesUiState())
    val uiState: StateFlow<AdminNotesUiState> = _uiState.asStateFlow()
    init {
        getUsersNotes()
    }

    fun getUsersNotes() = viewModelScope.launch {

        val result = userRepository.getAllPublicNotes()

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

    fun deleteUserPublicNote(note: BookNote)=viewModelScope.launch{
        userRepository.deleteUserPublicNote(note)
        getUsersNotes()
    }

    private fun updateUiState(block: AdminNotesUiState.() -> AdminNotesUiState) {
        _uiState.update(block)
    }
}
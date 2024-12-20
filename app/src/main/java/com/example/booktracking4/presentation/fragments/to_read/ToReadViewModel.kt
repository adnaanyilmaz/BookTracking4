package com.example.booktracking4.presentation.fragments.to_read

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToReadViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(ToReadUiState())
    val uiState: StateFlow<ToReadUiState> = _uiState.asStateFlow()

    fun fetchUserBooks() = viewModelScope.launch {
        val result = userRepository.getUserBooks(userId = authRepository.getUserId())
        when (result) {
            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }

            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }
                Log.e("Dante", "Error  viewmodel")
            }

            is Resource.Success -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        toRead = result.data?.wantToRead ?: emptyList(),
                    )

                }
            }
        }
    }
    fun deleteUserBook(bookId: String)=viewModelScope.launch{
        userRepository.deleteUserBooks(userId = authRepository.getUserId(), bookId = bookId)
        fetchUserBooks()
    }


    private fun updateUiState(block: ToReadUiState.() -> ToReadUiState) {
        _uiState.update(block)

    }


}
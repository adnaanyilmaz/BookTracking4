package com.example.booktracking4.presentation.fragments.read

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
class ReadViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(ReadUiState())
    val uiState: StateFlow<ReadUiState> = _uiState.asStateFlow()


    fun fetchUserBooks() = viewModelScope.launch {
        val result = userRepository.getUserBooks(userId = authRepository.getUserId())
        when (result) {
            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }

            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }
                Log.e("Dante", result.message.toString())
            }

            is Resource.Success -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        read = result.data?.read ?: emptyList(),
                    )

                }
            }
        }
    }
    fun deleteUserBook(bookId: String)=viewModelScope.launch{
        userRepository.deleteUserBooks(
            userId = authRepository.getUserId(),
            bookId = bookId
            )
        fetchUserBooks()
    }
    fun upDateFavorite(bookId: String,newIsFavoriteStatus: Boolean)=viewModelScope.launch{
        userRepository.updateIsFavorite(
            userId = authRepository.getUserId(),
            bookId = bookId,
            newIsFavoriteStatus =newIsFavoriteStatus
        )
        fetchUserBooks()
    }

    fun filterFavoriteBooks()=viewModelScope.launch{
         val result=userRepository.getFavoriteBooks(
            userId = authRepository.getUserId()
        )
        when(result){
            is Resource.Error -> updateUiState {
                copy(
                    isLoading = true,
                    read = emptyList()
                )

            }
            is Resource.Loading -> updateUiState {
                copy(
                    isLoading = true,
                )

            }
            is Resource.Success -> updateUiState {
                copy(
                    isLoading = false,
                    read = result.data ?: emptyList(),
                )

            }
        }
    }


    private fun updateUiState(block: ReadUiState.() -> ReadUiState) {
        _uiState.update(block)
    }
}
package com.example.booktracking4.presentation.fragments.favorite_books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.read.ReadUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteBooksViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
    ): ViewModel() {
    private var _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()


init {
    filterFavoriteBooks()
}
    private fun filterFavoriteBooks()=viewModelScope.launch{
        val result=userRepository.getFavoriteBooks(
            userId = auth.currentUser?.uid!!
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

    fun upDateFavorite(bookId: String,newIsFavoriteStatus: Boolean)=viewModelScope.launch{
        userRepository.updateIsFavorite(
            userId = auth.currentUser?.uid!!,
            bookId = bookId,
            newIsFavoriteStatus =newIsFavoriteStatus
        )
        filterFavoriteBooks()
    }



    private fun updateUiState(block: FavoriteUiState.() -> FavoriteUiState) {
        _uiState.update(block)
    }
}
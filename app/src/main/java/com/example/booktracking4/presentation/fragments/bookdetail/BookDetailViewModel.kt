package com.example.booktracking4.presentation.fragments.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.ReadNow
import com.example.booktracking4.data.remote.user.WhatIRead
import com.example.booktracking4.data.remote.user.WhatIWillRead
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.domain.usecase.book_detail_usecase.GetBookDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookState())
    val state: StateFlow<BookState> = _state

//    private var _uiState = MutableStateFlow(BookState())
//    val uiState: StateFlow<BookState> = _uiState.asStateFlow()


    var id: String? = null
        set(value) {
            field=value
            if (!value.isNullOrEmpty()){
                getBook(id!!)
            }
        }

    // Kitap detayını almak için çağrılan fonksiyon
    fun getBook(id: String) {
        viewModelScope.launch {
            // Use case'den dönen Flow'u topluyoruz (collect)
            getBookDetailUseCase(id).collect { result ->
                _state.value = when (result) {
                    is Resource.Success -> BookState(book = result.data)
                    is Resource.Error -> BookState(
                        error = result.message ?: "Beklenmeyen bir hata oluştu"
                    )

                    is Resource.Loading -> BookState(isLoading = true)
                }
            }
        }
    }

    fun addBookWhatIRead(userId: String, book: WhatIRead) = viewModelScope.launch {
        val result = userRepository.addBookToWhatIRead(userId = userId, book = book)
        when (result) {
            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Success -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Loading -> { updateUiState { copy(isLoading = true) }}
        }
    }

    fun addBookWhatIWillRead(userId: String, book: WhatIWillRead) = viewModelScope.launch {
        val result = userRepository.addBookToWhatIWillRead(userId = userId, book = book)
        when (result) {
            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Success -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Loading -> { updateUiState { copy(isLoading = true) }}
        }
    }

    fun addBookReadNow(userId: String, book: ReadNow) = viewModelScope.launch {
        val result = userRepository.addBookToReadNow(userId = userId, book = book)
        when (result) {
            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Success -> {
                updateUiState { copy(isLoading = false) }

            }
            is Resource.Loading -> { updateUiState { copy(isLoading = true) }}
        }
    }

    fun getUserId(): String{
        return authRepository.getUserId()
    }

    private fun updateUiState(block: BookState.() -> BookState) {
        _state.update(block)
    }



}

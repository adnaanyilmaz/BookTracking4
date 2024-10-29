package com.example.booktracking4.presentation.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.usecase.GetBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel as HiltViewModel

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookState())
    val state: StateFlow<BookState> = _state

    init {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            getBookUseCase().collect { result ->
                _state.value = when (result) {
                    is Resource.Success -> BookState(book = result.data ?: emptyList())
                    is Resource.Error -> BookState(error = result.message ?: "An unexpected error occurred")
                    is Resource.Loading -> BookState(isLoading = true)
                }
            }
        }
    }
}

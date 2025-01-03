package com.example.booktracking4.presentation.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.usecase.search_use_cases.GetBookUseCase
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

    var searchWord: String? = null
        set(value) {
            field = value
            if (!value.isNullOrEmpty()) {
                getBooks(value)
            }
        }

    private fun getBooks(searchWord: String) {
        viewModelScope.launch {
            getBookUseCase(searchWord).collect { result ->
                _state.value = when (result) {
                    is Resource.Success -> BookState(book = result.data ?: emptyList())
                    is Resource.Error -> BookState(error = result.message ?: "An unexpected error occurred")
                    is Resource.Loading -> BookState(isLoading = true)
                }
            }
        }
    }
}

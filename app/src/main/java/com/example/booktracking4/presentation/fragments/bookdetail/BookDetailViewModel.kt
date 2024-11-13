package com.example.booktracking4.presentation.fragments.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.usecase.book_detail_usecase.GetBookDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookState())
    val state: StateFlow<BookState> = _state

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
}

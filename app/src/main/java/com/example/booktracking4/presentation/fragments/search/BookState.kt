package com.example.booktracking4.presentation.fragments.search

import com.example.booktracking4.domain.model.retrofit.Book

data class BookState(
    val isLoading: Boolean = false,
    val book: List<Book> = emptyList(),
    val error: String = ""

)

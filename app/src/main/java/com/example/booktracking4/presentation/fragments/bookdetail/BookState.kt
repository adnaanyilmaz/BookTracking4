package com.example.booktracking4.presentation.fragments.bookdetail

import com.example.booktracking4.domain.model.retrofit.BookDetail


data class BookState(
    val isLoading: Boolean = false,
    val book: BookDetail ?=null,
    val error: String = ""

)

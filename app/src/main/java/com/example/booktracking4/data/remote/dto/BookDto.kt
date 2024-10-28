package com.example.booktracking4.data.remote.dto

import com.example.booktracking4.domain.model.Book

data class BookDto(
    val items: List<İtem>,
    val kind: String,
    val totalItems: Int
)

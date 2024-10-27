package com.example.booktracking4.data.remote.dto

data class BookDto(
    val items: List<İtem>,
    val kind: String,
    val totalItems: Int
)
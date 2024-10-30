package com.example.booktracking4.domain.model


import com.example.booktracking4.data.remote.dto.ImageLinks

data class Book(
    val authors: List<String>,
    val categories: List<String>,
    val imageLinks: ImageLinks,
    val title: String
) {}
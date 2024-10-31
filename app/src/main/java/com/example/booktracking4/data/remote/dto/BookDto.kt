package com.example.booktracking4.data.remote.dto

import com.example.booktracking4.domain.model.Book

data class BookDto(
    val items: List<Item>?,
    val kind: String?,
    val totalItems: Int?
){
}
// BookDto.kt
fun BookDto.toBooks(): List<Book> {
    return items?.mapNotNull { it.toBook() } ?: emptyList()
}



package com.example.booktracking4.domain.model.retrofit
import com.example.booktracking4.data.remote.dto.detail_dto.ImageLinks
import com.example.booktracking4.data.remote.dto.detail_dto.IndustryIdentifier

data class BookDetail(
    val authors: List<String>?,
    val categories: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val industryIdentifiers: List<IndustryIdentifier>?,//isbn
    val pageCount: Int?,
    val publishedDate: String?,
    val publisher: String?,
    val ratingsCount: Int?,
    val title: String?
)

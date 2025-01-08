package com.example.booktracking4.domain.model.retrofit

data class CategoriesRecommended(
    val authors: List<String>,
    val id: String,
    val categories: List<String>,
    val imageLink: String?,
    val title: String
)

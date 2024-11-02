package com.example.booktracking4.domain.model.retrofit


import androidx.room.PrimaryKey
import com.example.booktracking4.data.remote.dto.search_dto.ImageLinks

data class Book(
    val authors: List<String>,
    val categories: List<String>,
    val imageLinks: ImageLinks,
    val title: String
) {}
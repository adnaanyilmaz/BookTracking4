package com.example.booktracking4.data.remote.user


data class Read(
    val bookId: String = "",
    val bookName: String = "",
    val authorName: String = "",
    val image: String = "",
    val pageCount: Int? =null,
    val category: String = "",
    var isFavorite: Boolean=false
)

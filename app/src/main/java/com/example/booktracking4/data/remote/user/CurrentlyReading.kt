package com.example.booktracking4.data.remote.user

data class CurrentlyReading(
    val bookId: String = "",
    val bookName: String = "",
    val authorName: String = "",
    val image: String = "",
    val pageCount: Int?=null
)

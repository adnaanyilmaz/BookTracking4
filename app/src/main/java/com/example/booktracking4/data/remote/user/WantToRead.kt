package com.example.booktracking4.data.remote.user

data class WantToRead(
    val bookId: String = "",
    val authorName: String = "",
    val bookName: String = "",
    val image: String = "",
    val pageCount: Int?=null

)
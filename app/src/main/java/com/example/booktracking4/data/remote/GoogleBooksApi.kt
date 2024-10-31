package com.example.booktracking4.data.remote

import com.example.booktracking4.data.remote.dto.BookDto
import retrofit2.http.GET
import retrofit2.http.Query

// GET https://www.googleapis.com/books/v1/volumes?q=suç ve ceza&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ
interface GoogleBooksApi {
    @GET("v1/volumes")
    suspend fun getBook(
        @Query("q") searchWord: String,
        @Query("key") apiKey: String = "AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ"
    ): BookDto
}

package com.example.booktracking4.data.remote

import com.example.booktracking4.common.Constants.API_KEY
import com.example.booktracking4.data.remote.dto.detail_dto.BookDetailDto
import com.example.booktracking4.data.remote.dto.search_dto.BookDto
import com.example.booktracking4.domain.model.retrofit.BookDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// GET https://www.googleapis.com/books/v1/volumes?q=suç ve ceza&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ
// Detail ==> https://www.googleapis.com/books/v1/volumes/VtYsGgAACAAJ?key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ
interface GoogleBooksApi {
    @GET("v1/volumes")
    suspend fun getBooks(
        @Query("q") searchWord: String,
        @Query("key") apiKey: String = API_KEY
    ): BookDto

    @GET("v1/volumes/{id}")
    suspend fun getBookDetail(
        @Path("id") id: String
    ): BookDetailDto
}

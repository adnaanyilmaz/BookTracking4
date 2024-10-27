package com.example.booktracking4.data.remote

import retrofit2.http.GET

// GET https://www.googleapis.com/books/v1/volumes?q=suç ve ceza&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ
interface GoogleBooksApi {
    @GET(value ="/v1/volumes?q=suç ve ceza&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ")
    suspend fun getBook()
}
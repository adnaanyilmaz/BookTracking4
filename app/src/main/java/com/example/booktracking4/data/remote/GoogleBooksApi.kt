package com.example.booktracking4.data.remote

import com.example.booktracking4.data.remote.dto.VolumeInfo
import retrofit2.http.GET
import retrofit2.http.Path

// GET https://www.googleapis.com/books/v1/volumes?q=suç ve ceza&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ
interface GoogleBooksApi {
    @GET(value ="/v1/volumes?q={searchWord}&key=AIzaSyAGKn0Rrj9dBjNlWwkBIgrB_MiMooGe1wQ")
    suspend fun getBook(@Path("searchWord") searchWord:String ):List<VolumeInfo>
}
package com.example.booktracking4.domain.repository

import com.example.booktracking4.data.remote.dto.search_dto.BookDto

interface BookRepository {

    suspend fun getBook(searchWord:String): BookDto

}
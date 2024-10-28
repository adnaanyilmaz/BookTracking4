package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.remote.dto.BookDto
import com.example.booktracking4.data.remote.dto.VolumeInfo
import com.example.booktracking4.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api:GoogleBooksApi
):BookRepository{
    override suspend fun getBook(searchWord: String): List<VolumeInfo> {
        return api.getBook(searchWord)
    }


}
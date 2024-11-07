package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.remote.dto.detail_dto.BookDetailDto
import com.example.booktracking4.data.remote.dto.search_dto.BookDto
import com.example.booktracking4.domain.model.retrofit.BookDetail

import com.example.booktracking4.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val api:GoogleBooksApi
):BookRepository{
    override suspend fun getBook(searchWord: String): BookDto {
        return api.getBooks(searchWord)
    }

    override suspend fun getBookDetail(id: String): BookDetailDto {
        return api.getBookDetail(id)
    }
}
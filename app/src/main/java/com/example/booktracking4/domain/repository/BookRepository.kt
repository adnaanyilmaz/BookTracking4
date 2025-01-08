package com.example.booktracking4.domain.repository

import com.example.booktracking4.data.remote.dto.detail_dto.BookDetailDto
import com.example.booktracking4.data.remote.dto.search_dto.BookDto
import com.example.booktracking4.domain.model.retrofit.CategoriesRecommended

interface BookRepository {

    suspend fun getBook(searchWord:String): BookDto

    suspend fun getBookDetail(id:String): BookDetailDto

    suspend fun getBooksByCategory(category: String): List<CategoriesRecommended>


}
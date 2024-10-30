package com.example.booktracking4.domain.repository

import com.example.booktracking4.data.remote.dto.BookDto
import com.example.booktracking4.data.remote.dto.Item
import com.example.booktracking4.data.remote.dto.VolumeInfo

interface BookRepository {

    suspend fun getBook(searchWord:String): BookDto

}
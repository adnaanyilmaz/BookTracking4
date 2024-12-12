package com.example.booktracking4.domain.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.ReadNow
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.WhatIRead
import com.example.booktracking4.data.remote.user.WhatIWillRead
import com.example.booktracking4.domain.model.ui_model.search_model.Book

interface UserRepository {

    suspend fun addUser(user: User): Resource<String>
    suspend fun addBookToWhatIRead(userId: String, book: WhatIRead): Resource<String>
    suspend fun addBookToWhatIWillRead(userId: String, book: WhatIWillRead): Resource<String>
    suspend fun addBookToReadNow(userId: String, book: ReadNow): Resource<String>
    suspend fun getUserBooks(userId: String): Resource<User>
    suspend fun getUserForBooks(bookId: String): Resource<List<Book>>
    suspend fun getUserName(uid: String): Resource<String>

}
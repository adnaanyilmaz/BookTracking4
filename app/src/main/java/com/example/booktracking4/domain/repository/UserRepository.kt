package com.example.booktracking4.domain.repository

import android.R
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.UserCategories
import com.example.booktracking4.data.remote.user.WantToRead
import com.example.booktracking4.domain.model.ui_model.search_model.Book

interface UserRepository {

    suspend fun addUser(user: User): Resource<String>
    suspend fun addBookToRead(userId: String, book: Read): Resource<String>
    suspend fun addBookToWantToRead(userId: String, book: WantToRead): Resource<String>
    suspend fun addBookToCurrentlyReading(userId: String, book: CurrentlyReading): Resource<String>
    suspend fun deleteUserBooks(userId: String, bookId: String): Resource<User>
    suspend fun getUserBooks(userId: String): Resource<User>
    suspend fun getUserName(uid: String): Resource<String>
    suspend fun checkUserName(userName: String): Boolean
    suspend fun addUserCategories(userId: String, category: UserCategories): Resource<String>
    suspend fun getUserCategories(uid: String): Resource<List<UserCategories>>

}
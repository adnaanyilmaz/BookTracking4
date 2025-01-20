package com.example.booktracking4.data.remote.user

import com.example.booktracking4.domain.model.room.BookNote

data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val read: List<Read> = emptyList(),
    val wantToRead: List<WantToRead> = emptyList(),
    val currentlyReading: List<CurrentlyReading> = emptyList(),
    val friend: List<Friends> = emptyList(),
    val friendsRequest: List<FriendsRequest> = emptyList(),
    val notes: List<BookNote> = emptyList(),
    val userCategory: List<UserCategories> = emptyList(),
    val admin: Boolean = false
)

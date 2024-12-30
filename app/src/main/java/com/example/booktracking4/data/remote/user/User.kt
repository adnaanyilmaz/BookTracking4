package com.example.booktracking4.data.remote.user

data class User(
    val uid: String = "",
    val userName: String = "",
    val friend: List<Friend> =emptyList(),
    val email: String = "",
    val read: List<Read> = emptyList(),
    val wantToRead: List<WantToRead> = emptyList(),
    val currentlyReading: List<CurrentlyReading> = emptyList()
)

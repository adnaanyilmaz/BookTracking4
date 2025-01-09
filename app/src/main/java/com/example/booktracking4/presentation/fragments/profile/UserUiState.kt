package com.example.booktracking4.presentation.fragments.profile

import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.Read

data class UserUiState (
    val userName: String="",
    val email: String=""
)
data class BookUiState(
    val read: List<Read> = emptyList(),
    val currentlyReading: List<CurrentlyReading> = emptyList()
)
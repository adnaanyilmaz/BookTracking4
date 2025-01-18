package com.example.booktracking4.presentation.fragments.friends_notes

import com.example.booktracking4.domain.model.room.BookNote

data class FriendNotesUiState(
    val isLoading: Boolean = true,
    val error: String = "",
    val notes: List<BookNote> = emptyList(),
    val userName: String=""
)

package com.example.booktracking4.presentation.fragments.friends_notes

import com.example.booktracking4.domain.model.room.BookNote

data class FriendNotesUiState(
    val isLoading: Boolean = true,
    val error: String = "",
    val notesWithUsernames: List<Pair<String, BookNote>> = emptyList() // Username ve not eşleşmeleri
)


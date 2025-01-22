package com.example.booktracking4.presentation.fragments.admin_notes

import com.example.booktracking4.domain.model.room.BookNote

data class AdminNotesUiState(
    val isLoading: Boolean = true,
    val error: String = "",
    val notesWithUsernames: List<Pair<String, BookNote>> = emptyList() // Username ve not eşleşmeleri
)


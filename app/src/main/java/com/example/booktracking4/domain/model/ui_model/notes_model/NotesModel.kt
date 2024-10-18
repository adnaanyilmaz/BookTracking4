package com.example.booktracking4.domain.model.ui_model.notes_model

data class NotesModel(
    val id: Int,
    val title: String,
    val pageRange: String,
    val content: String,
    val date: String,
    val isFavorite: Boolean
) {
}
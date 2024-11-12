package com.example.booktracking4.domain.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BookNote(
    val title: String,
    val content: String,
    val timestamp: Long,
    val page: String,
    val isFavorite: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {

    }
}
class InvalidNoteException(message: String): Exception(message)
package com.example.booktracking4.domain.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "BookNote")
data class BookNote(
    val bookName: String? = null,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val page: String = "",
    var isFavorite: Boolean = false,
    val userId: String = "",
    @PrimaryKey val id: Int? = null
) {
    // Firestore için varsayılan bir constructor gerekli
    constructor() : this(
        bookName = null,
        title = "",
        content = "",
        timestamp = 0L,
        page = "",
        isFavorite = false,
        userId = "",
        id = null
    )
}

class InvalidNoteException(message: String) : Exception(message)

package com.example.booktracking4.domain.repository

import com.example.booktracking4.domain.model.room.BookNote
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getAllNotesFromRoom(): Flow<List<BookNote>>
    suspend fun syncNotesWithFirebase(uid: String, notes: List<BookNote>)
}

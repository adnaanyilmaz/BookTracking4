package com.example.booktracking4.domain.repository

import com.example.booktracking4.domain.model.room.BookNote
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getAllNotesFromRoom(uid: String): Flow<List<BookNote>>
    suspend fun syncNotesWithFirebase(uid: String, notes: List<BookNote>)
    suspend fun fetchNotesFromFirebase(uid: String): List<BookNote>
    suspend fun syncRoomWithFirebase(uid: String)
}

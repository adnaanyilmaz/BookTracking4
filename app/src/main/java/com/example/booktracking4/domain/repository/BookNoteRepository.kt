package com.example.booktracking4.domain.repository

import com.example.booktracking4.domain.model.room.BookNote
import kotlinx.coroutines.flow.Flow

interface BookNoteRepository {

    fun getNotes(): Flow<List<BookNote>>

    suspend fun getNoteById(id:Int): BookNote?

    suspend fun insetNote(note: BookNote)

    suspend fun deleteNote(note: BookNote)


}
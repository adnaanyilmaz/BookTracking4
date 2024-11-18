package com.example.booktracking4.data.data_resourse.repository

import com.example.booktracking4.data.data_resourse.NoteDao
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository
import kotlinx.coroutines.flow.Flow

class BookNoteRepositoryImp(
    private val dao: NoteDao
): BookNoteRepository {
    override fun getNotes(): Flow<List<BookNote>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int?): BookNote? {
        return dao.getNoteById(id)
    }

    override suspend fun insetNote(note: BookNote) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: BookNote) {
        return dao.deteleNote(note)
    }

    override suspend fun updateNote(note: BookNote) {
        return dao.updateNote(note)
    }
}
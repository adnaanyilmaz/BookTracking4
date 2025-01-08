package com.example.booktracking4.domain.usecase.note_use_cases

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository
import com.example.booktracking4.domain.repository.NotesRepository


class EditNoteUseCase(
    private val repository: BookNoteRepository,
    private val firebaseRepository: NotesRepository
) {
    suspend operator fun invoke(id: Int?): BookNote? {
        val localNote = repository.getNoteById(id)
        val firebaseNote = firebaseRepository.getNoteById(id)

        return firebaseNote
    }
}

package com.example.booktracking4.domain.usecase.note_use_cases

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository
import com.example.booktracking4.domain.repository.NotesRepository

class UpdateFavoriteStatusUseCase(
    private val repository: BookNoteRepository,
    private val firebaseRepository: NotesRepository
    ) {

    suspend operator fun invoke(note: BookNote){
        val updateNote=note.copy(isFavorite = !note.isFavorite)
        repository.updateNote(updateNote)
        firebaseRepository.updateNote(updateNote)
    }
}
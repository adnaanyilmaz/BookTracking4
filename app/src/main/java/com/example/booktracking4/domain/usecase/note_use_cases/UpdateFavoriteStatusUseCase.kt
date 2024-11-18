package com.example.booktracking4.domain.usecase.note_use_cases

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository

class UpdateFavoriteStatusUseCase(private val repository: BookNoteRepository) {

    suspend operator fun invoke(note: BookNote){
        val updateNote=note.copy(isFavorite = !note.isFavorite)
        repository.updateNote(updateNote)
    }
}
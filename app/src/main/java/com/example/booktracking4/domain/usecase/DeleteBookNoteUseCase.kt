package com.example.booktracking4.domain.usecase

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository
import com.example.booktracking4.domain.repository.BookRepository

class DeleteBookNoteUseCase(
    private val repository: BookNoteRepository

) {
    suspend operator fun invoke(note: BookNote){
        repository.deleteNote(note)
    }
}
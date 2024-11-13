package com.example.booktracking4.domain.usecase.note_use_cases

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.BookNoteRepository

class EditNoteUseCase(
    private val repository: BookNoteRepository
){
    suspend operator fun invoke(id: Int?): BookNote?{
        return repository.getNoteById(id)
    }
}
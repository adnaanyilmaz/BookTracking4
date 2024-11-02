package com.example.booktracking4.domain.usecase

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.model.room.InvalidNoteException
import com.example.booktracking4.domain.repository.BookNoteRepository

class AddBookNoteUseCase(
    private val repository: BookNoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: BookNote){
        if(note.title.isBlank()){
            throw InvalidNoteException("The title of the note can't be empty.")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("The content of the note can't be empty")
        }
        repository.insetNote(note)
    }
}
package com.example.booktracking4.domain.usecase.note_use_cases

data class BookNoteUseCases(
    val getBookNoteUseCase: GetBookNoteUseCase,
    val deleteBookNoteUseCase: DeleteBookNoteUseCase,
    val addNote: AddBookNoteUseCase,
    val editNote: EditNoteUseCase
){}

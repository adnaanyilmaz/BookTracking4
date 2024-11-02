package com.example.booktracking4.domain.usecase

data class BookNoteUseCases(
    val getBookNoteUseCase: GetBookNoteUseCase,
    val deleteBookNoteUseCase: DeleteBookNoteUseCase,
    val addNote: AddBookNoteUseCase
){}

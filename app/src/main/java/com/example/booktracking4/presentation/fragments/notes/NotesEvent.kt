package com.example.booktracking4.presentation.fragments.notes

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: BookNote): NotesEvent()
    data class ToggleFavorite(val note: BookNote) : NotesEvent()
    object RestoreNote: NotesEvent()

}
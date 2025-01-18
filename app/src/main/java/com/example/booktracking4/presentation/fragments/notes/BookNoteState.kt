package com.example.booktracking4.presentation.fragments.notes

import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType

data class BookNoteState(
    val notes: List<BookNote> = emptyList(),
    val bookNames: List<String> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false
)

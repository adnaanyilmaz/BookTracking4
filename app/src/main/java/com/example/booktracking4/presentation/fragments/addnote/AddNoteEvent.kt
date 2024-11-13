package com.example.booktracking4.presentation.fragments.addnote

sealed class AddNoteEvent {
    data class EnteredTitle(val value: String) : AddNoteEvent()
    data class ChangeTitleFocus(val hasFocus: Boolean) : AddNoteEvent()
    data class EnteredContent(val value: String) : AddNoteEvent()
    data class ChangeContentFocus(val hasFocus: Boolean) : AddNoteEvent()
    data class EnteredPageNumber(val value: String) : AddNoteEvent()
    data class ChangePageNumberFocus(val hasFocus: Boolean) : AddNoteEvent()
    data class isFavorite(val value: Boolean) : AddNoteEvent()
    object SaveNote : AddNoteEvent()
}
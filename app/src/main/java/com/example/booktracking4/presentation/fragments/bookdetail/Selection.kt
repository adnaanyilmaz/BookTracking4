package com.example.booktracking4.presentation.fragments.bookdetail

sealed class Selection {
    data object WantToRead : Selection()
    data object CurrentlyReading: Selection()
    data object Read : Selection()
}

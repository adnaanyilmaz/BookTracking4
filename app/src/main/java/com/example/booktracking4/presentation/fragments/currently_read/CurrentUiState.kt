package com.example.booktracking4.presentation.fragments.currently_read

import com.example.booktracking4.data.remote.user.CurrentlyReading

data class CurrentUiState (
    val isLoading: Boolean = true,
    val currentlyReading: List<CurrentlyReading> = emptyList()
)
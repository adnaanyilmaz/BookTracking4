package com.example.booktracking4.presentation.fragments.home

import com.example.booktracking4.data.remote.user.CurrentlyReading

data class HomeUiState (
    val isLoading: Boolean = true,
    val currentlyReading: List<CurrentlyReading> = emptyList()
)
package com.example.booktracking4.presentation.fragments.currently_read

import com.example.booktracking4.data.remote.user.ReadNow

data class CurrentUiState (
    val isLoading: Boolean = true,
    val readNow: List<ReadNow> = emptyList()
)
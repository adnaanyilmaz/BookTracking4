package com.example.booktracking4.presentation.fragments.read

import com.example.booktracking4.data.remote.user.Read

data class ReadUiState(
    val isLoading: Boolean = true,
    val read: List<Read> = emptyList()
)

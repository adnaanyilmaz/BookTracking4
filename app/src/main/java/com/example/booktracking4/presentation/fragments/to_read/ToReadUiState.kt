package com.example.booktracking4.presentation.fragments.to_read

import com.example.booktracking4.data.remote.user.WantToRead

data class ToReadUiState(
    val isLoading: Boolean = true,
    val toRead: List<WantToRead> = emptyList()
)
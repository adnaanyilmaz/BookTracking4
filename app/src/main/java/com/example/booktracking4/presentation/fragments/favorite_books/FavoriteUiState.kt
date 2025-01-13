package com.example.booktracking4.presentation.fragments.favorite_books

import com.example.booktracking4.data.remote.user.Read

data class FavoriteUiState(
    val isLoading: Boolean = true,
    val read: List<Read> = emptyList()
)

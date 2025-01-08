package com.example.booktracking4.presentation.fragments.home

import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.Friends
import com.example.booktracking4.domain.model.retrofit.CategoriesRecommended

data class HomeUiState (
    val isLoading: Boolean = true,
    val currentlyReading: List<CurrentlyReading> = emptyList(),
)
data class RecommendedUiState (
    val isLoading: Boolean = true,
    val recommendedList: List<CategoriesRecommended> = emptyList(),
)
data class RecommendedFriendUiState (
    val isLoading: Boolean = true,
    val recommendedList: List<CategoriesRecommended> = emptyList(),
    val friend: String? = ""
)
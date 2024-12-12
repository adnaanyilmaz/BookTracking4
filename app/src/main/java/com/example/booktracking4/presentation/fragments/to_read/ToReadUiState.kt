package com.example.booktracking4.presentation.fragments.to_read

import com.example.booktracking4.data.remote.user.WhatIWillRead

data class ToReadUiState(
    val isLoading: Boolean = true,
    val willRead: List<WhatIWillRead> = emptyList()
)
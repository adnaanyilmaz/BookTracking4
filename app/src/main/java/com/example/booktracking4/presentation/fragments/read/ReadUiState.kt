package com.example.booktracking4.presentation.fragments.read

import com.example.booktracking4.data.remote.user.WhatIRead

data class ReadUiState(
    val isLoading: Boolean = true,
    val whatIRead: List<WhatIRead> = emptyList()
)

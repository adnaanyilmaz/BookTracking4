package com.example.booktracking4.presentation.fragments.admin_users

import com.example.booktracking4.data.remote.user.User

sealed class AdminUserUiState {
    data class Success(val requests: List<User>): AdminUserUiState()
    data class Error(val message: String): AdminUserUiState()
    object Loading: AdminUserUiState()
    object Idle: AdminUserUiState()
}
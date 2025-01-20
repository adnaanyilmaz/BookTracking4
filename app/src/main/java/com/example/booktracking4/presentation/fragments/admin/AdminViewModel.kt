package com.example.booktracking4.presentation.fragments.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authRepository: AuthRepository,

    ) : ViewModel() {


    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
    }
}
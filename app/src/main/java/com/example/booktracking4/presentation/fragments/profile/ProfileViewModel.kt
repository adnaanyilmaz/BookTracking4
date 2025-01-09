package com.example.booktracking4.presentation.fragments.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.profile.ProfileContract.UiEffect
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private val _userState = MutableStateFlow(UserUiState())
    val userState: StateFlow<UserUiState> = _userState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getUser()
        fetchUserBooks()
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut()
        emitUiEffect(UiEffect.GoToLoginScreen)
        emitUiEffect(UiEffect.ShowToastMessage("Logged out successfully."))
    }

    fun getUser() = viewModelScope.launch() {
        val result = userRepository.getUserName(auth.currentUser?.uid!!)
        when (result) {
            is Resource.Error -> TODO()
            is Resource.Loading -> TODO()
            is Resource.Success -> {
                updateUiState {
                    copy(
                        userName = result.data.toString(),
                        email = auth.currentUser?.email!!
                    )
                }
            }
        }
    }

    fun fetchUserBooks() = viewModelScope.launch {
        val result = userRepository.getUserBooks(userId = authRepository.getUserId())
        when (result) {
            is Resource.Loading -> {}

            is Resource.Error -> {
                Log.e("Dante", result.message.toString())
            }

            is Resource.Success -> {
                updateBookUiState {
                    copy(
                        read = result.data?.read.orEmpty(),
                        currentlyReading = result.data?.currentlyReading.orEmpty()
                    )

                }
            }
        }
    }


    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }

    private fun updateUiState(block: UserUiState.() -> UserUiState) {
        _userState.update(block)
    }

    private fun updateBookUiState(block: BookUiState.() -> BookUiState) {
        _uiState.update(block)
    }
}
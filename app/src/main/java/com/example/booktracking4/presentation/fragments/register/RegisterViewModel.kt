package com.example.booktracking4.presentation.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.presentation.fragments.register.RegisterContract.RegisterUiEffect
import com.example.booktracking4.presentation.fragments.register.RegisterContract.RegisterUiState
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    private val _uiEffect by lazy { Channel<RegisterUiEffect>() }
    val uiEffect: Flow<RegisterUiEffect>by lazy { _uiEffect.receiveAsFlow() }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        when (val result = authRepository.signUp(
            email = email,
            password = password
        )) {
            is Resource.Success -> {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(RegisterUiEffect.ShowToastMessage("Success: ${result.data}"))
                emitUiEffect(RegisterUiEffect.GoToSignInScreen) // Hesap oluşturulursa tekrar login olacak kullınıcı yada istersen main ekranına da yönlendirebilirsin
            }
            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }
            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(RegisterUiEffect.ShowToastMessage("Error: ${result.message}"))
            }
        }
    }

    private fun updateUiState(block: RegisterUiState.() -> RegisterUiState) {
        _registerUiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: RegisterUiEffect) {
        _uiEffect.send(uiEffect)
    }

}
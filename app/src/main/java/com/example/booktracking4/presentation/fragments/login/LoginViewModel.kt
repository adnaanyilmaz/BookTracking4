package com.example.booktracking4.presentation.fragments.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.login.LoginContract.LoginUiEffect
import com.example.booktracking4.presentation.fragments.login.LoginContract.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _uiEffect by lazy { Channel<LoginUiEffect>() }
    val uiEffect: Flow<LoginUiEffect> by lazy { _uiEffect.receiveAsFlow() }


    fun signIn(email: String, password: String) = viewModelScope.launch {
        when (val result = authRepository.signIn(email = email, password = password)) {
            is Resource.Success -> {
                emitUiEffect(LoginUiEffect.ShowToast(result.data.orEmpty()))
                updateUiState { copy(isLoading = false) }

                val userId = authRepository.getUserId()
                val isAdmin = userRepository.isAdmin(userId).first()

                if (isAdmin) {
                    emitUiEffect(LoginUiEffect.GoToAdminScreen)
                } else {
                    emitUiEffect(LoginUiEffect.GoToHomeScreen)
                }

            }

            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }

            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(LoginUiEffect.ShowToast(result.message.orEmpty()))
            }
        }
    }


    private fun updateUiState(block: LoginUiState.() -> LoginUiState) {
        _loginUiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: LoginUiEffect) {
        _uiEffect.send(uiEffect)
    }
}
package com.example.booktracking4.presentation.fragments.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.login.LoginContract.LoginUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import com.example.booktracking4.presentation.fragments.splash.SplashContract.SplashUiEffect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _splashUiEffect by lazy { Channel<SplashUiEffect>() }
    val splashUiEffect: Flow<SplashUiEffect> by lazy { _splashUiEffect.receiveAsFlow() }

    init {
        isLoggedIn()
    }

    private fun isLoggedIn() = viewModelScope.launch{

        if (authRepository.isUserLoggedIn()) {
            val currentUserId = authRepository.getUserId()
            val userName = userRepository.getUserName(uid = currentUserId)

            val isAdmin = userRepository.isAdmin(currentUserId).first()

            if (isAdmin) {
                emitUiEffect(SplashUiEffect.GoToAdminScreen)
            } else {
                emitUiEffect(SplashUiEffect.GoToMainScreen)
            }
            emitUiEffect(SplashUiEffect.ShowToast("Welcome: ${userName.data}"))
        } else {
            emitUiEffect(SplashUiEffect.GoToSignInScreen)
            emitUiEffect(SplashUiEffect.ShowToast("Please Login or Register"))
        }
    }




    private suspend fun emitUiEffect(uiEffect: SplashUiEffect) {
        _splashUiEffect.send(uiEffect)
    }
}
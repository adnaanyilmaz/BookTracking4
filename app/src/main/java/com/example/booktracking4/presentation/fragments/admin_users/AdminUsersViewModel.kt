package com.example.booktracking4.presentation.fragments.admin_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
private val repository: UserRepository
): ViewModel(){

    private val _userState = MutableStateFlow<AdminUserUiState>(AdminUserUiState.Idle)
    val userState: StateFlow<AdminUserUiState> get() = _userState

    init {
        getAllUsers()
    }

    fun getAllUsers()=viewModelScope.launch{
        val result=repository.getAllUsers()
        when(result){
            is Resource.Error -> _userState.value= AdminUserUiState.Error(result.message.toString())
            is Resource.Loading -> {}
            is Resource.Success -> _userState.value= AdminUserUiState.Success(result.data ?:emptyList())
        }
    }

    fun deleteUser(uid: String)=viewModelScope.launch{
        repository.deleteUser(uid)
        getAllUsers()
    }
}
package com.example.booktracking4.presentation.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.domain.repository.BookRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.example.booktracking4.presentation.fragments.friends.FriendsUiState
import com.google.android.play.integrity.internal.b
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val bookRepository: BookRepository,
    private val repository: AddFriendsRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var _recommendedUiState = MutableStateFlow(RecommendedUiState())
    val recommendedUiState: StateFlow<RecommendedUiState> = _recommendedUiState.asStateFlow()

    private var _recommendedFriendUiState = MutableStateFlow(RecommendedFriendUiState())
    val recommendedFriendUiState: StateFlow<RecommendedFriendUiState> =
        _recommendedFriendUiState.asStateFlow()

    init {
        fetchUserBooks()
        getCategoriesAndFetchRecommendedBooks()
        getRandomFriendAndBooks()
        getFriendsCategoriesAndFetchRecommendedBooks("Qk7Ah8gdR9gJzPI4t2ma6jXRGFK2")
    }


    private fun fetchUserBooks() = viewModelScope.launch {
        val result = userRepository.getUserBooks(userId = authRepository.getUserId())
        when (result) {
            is Resource.Loading -> {
                updateUiState { copy(isLoading = true) }
            }

            is Resource.Error -> {
                updateUiState { copy(isLoading = false) }
                Log.e("Dante", "Error  viewmodel")
            }

            is Resource.Success -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        currentlyReading = result.data?.currentlyReading ?: emptyList(),
                    )

                }
            }
        }
    }


    fun getRandomFriendAndBooks() = viewModelScope.launch {
        val result = repository.getFriendsList(uid = authRepository.getUserId())
        when (result) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {

                if (result.data?.isNotEmpty() == true) {
                    val randomFriend = result.data.random()
                    val randomFriendUid = randomFriend.uid
                    getFriendsCategoriesAndFetchRecommendedBooks(friendUid = randomFriendUid)


                }
            }
        }
    }

    private fun getCategoriesAndFetchRecommendedBooks() = viewModelScope.launch() {
        val result = userRepository.getUserCategories(uid = authRepository.getUserId())
        val categories: MutableList<String> = mutableListOf()

        when (result) {
            is Resource.Loading -> {
            }

            is Resource.Error -> {
            }

            is Resource.Success -> {
                result.data?.forEach() {
                    categories.add(it.userCategoryName)
                }

            }
        }
        if (categories.isNotEmpty()) {
            val randomCategory = categories.random().substringBefore("/")
            if (randomCategory.isNotEmpty()) {
                val result = bookRepository.getBooksByCategory(category = randomCategory)
                updateRecommendedUiState { copy(isLoading = false, recommendedList = result) }
                Log.e("Dante", "UserBooks: $result")
            }
        }
    }

    private fun getFriendsCategoriesAndFetchRecommendedBooks(friendUid: String) =
        viewModelScope.launch() {
            val result = userRepository.getUserCategories(uid = friendUid)
            val categories: MutableList<String> = mutableListOf()

            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Error -> {
                }

                is Resource.Success -> {
                    result.data?.forEach() {
                        categories.add(it.userCategoryName)
                    }

                }
            }
            if (categories.isNotEmpty()) {
                val randomCategory = categories.random().substringBefore("/")
                if (randomCategory.isNotEmpty()) {
                    val result = bookRepository.getBooksByCategory(category = randomCategory)
                    val getFriendUserNameResult = userRepository.getUserName(friendUid)
                    when (getFriendUserNameResult) {
                        is Resource.Success -> {
                            updateFriendRecommendedUiState {
                                copy(
                                    isLoading = false,
                                    recommendedList = result,
                                    friend = getFriendUserNameResult.data.orEmpty()
                                )
                            }
                            Log.e("Dante", "friendBooks: $result friendName: ${getFriendUserNameResult.data}")
                        }

                        is Resource.Error -> {}
                        is Resource.Loading -> {}
                    }


                }
            }
        }

    private fun updateUiState(block: HomeUiState.() -> HomeUiState) {
        _uiState.update(block)

    }

    private fun updateRecommendedUiState(block: RecommendedUiState.() -> RecommendedUiState) {
        _recommendedUiState.update(block)

    }


    private fun updateFriendRecommendedUiState(block: RecommendedFriendUiState.() -> RecommendedFriendUiState) {
        _recommendedFriendUiState.update(block)

    }
}
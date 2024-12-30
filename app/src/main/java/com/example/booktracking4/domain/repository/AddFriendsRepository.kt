package com.example.booktracking4.domain.repository

import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.User

interface AddFriendsRepository {
    suspend fun addFriend(currentUserId: String, friendUserName: String): Result<String>
    suspend fun searchFriendByUsername(userName: String): Result<List<User>>
    suspend fun sendFriendRequest(currentUserId: String, friendUserName: String): Result<String>
    suspend fun acceptFriendRequest(currentUserId: String, requestId: String): Result<String>
    suspend fun getFriendsBooks(userId: String): Result<List<Read>>
}
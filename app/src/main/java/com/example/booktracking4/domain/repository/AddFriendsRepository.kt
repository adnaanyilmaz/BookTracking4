package com.example.booktracking4.domain.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.FriendRequest
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.User

interface AddFriendsRepository {
    suspend fun searchFriendByUsername(userName: String): Result<List<User>>
    suspend fun getFriendsBooks(userId: String): Result<List<Read>>
    suspend fun sendFriendRequest(senderUid: String, receiverUid: String): Resource<Unit>
    suspend fun acceptFriendRequest(currentUid: String, senderUid: String): Resource<Unit>
    suspend fun rejectFriendRequest(currentUid: String, senderUid: String): Resource<Unit>
    suspend fun getFriendRequests(currentUid: String): Resource<List<FriendRequest>>
}
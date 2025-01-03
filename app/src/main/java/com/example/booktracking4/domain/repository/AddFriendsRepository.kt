package com.example.booktracking4.domain.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.Friends
import com.example.booktracking4.data.remote.user.FriendsRequest
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.User

interface AddFriendsRepository {
    /*
        Arkadaşlık isteği gönderme +
        Arkadaşlık isteği Kabul etme +
        Arkadaşlık isteği reddetme +
        Arkadaş listeleme +
        Arkadaşlık istekleri listeleme +
        Arkadaşlık Kitaplarını listeleme +

     */
    suspend fun searchFriendByUsername(userName: String): Result<List<User>>

    suspend fun sendFriendRequest(senderUid: String, receiverUserName: String): Result<String>
    suspend fun acceptFriendRequest(receiverUid: String, senderUserName: String): Resource<String>
    suspend fun rejectFriendRequest(receiverUid: String, senderUserName: String): Resource<String>
    suspend fun getFriendsList(uid: String): Resource<List<Friends>>
    suspend fun getFriendsRequests(uid: String): Resource<List<FriendsRequest>>
    suspend fun isUserInFriendsList(currentUserUid: String, friendUid: String): Boolean
}
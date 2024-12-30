package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.Friend
import com.example.booktracking4.data.remote.user.FriendRequest
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddFriendsRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
): AddFriendsRepository {
    override suspend fun addFriend(currentUserId: String, friendUserName: String): Result<String> {
        return try {
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("userName", friendUserName)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return Result.failure(Exception("Friend with username $friendUserName not found."))
            }

            val friendUser = querySnapshot.documents.first().let { documentToUser(it) }
                ?: return Result.failure(Exception("Invalid user data for $friendUserName."))

            val friend = Friend(uid = friendUser.uid, userName = friendUser.userName)
            val currentUserDocRef = firestore.collection("Users").document(currentUserId)

            firestore.runTransaction { transaction ->
                val currentUserSnapshot = transaction.get(currentUserDocRef)
                val currentUser = documentToUser(currentUserSnapshot)
                    ?: throw Exception("Current user not found.")

                val updatedFriends = currentUser.friend + friend
                transaction.update(currentUserDocRef, "friends", updatedFriends)
            }.await()

            Result.success("Friend ${friendUser.userName} added successfully.")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchFriendByUsername(userName: String): Result<List<User>> {
        return try {
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("userName", userName)
                .get()
                .await()

            val users = querySnapshot.documents.mapNotNull { documentToUser(it) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendFriendRequest(currentUserId: String, friendUserName: String): Result<String> {
        return try {
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("userName", friendUserName)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return Result.failure(Exception("User with username $friendUserName not found."))
            }

            val friendUser = querySnapshot.documents.first().let { documentToUser(it) }
                ?: return Result.failure(Exception("Invalid user data for $friendUserName."))

            val request = FriendRequest(fromUserId = currentUserId, toUserId = friendUser.uid)
            firestore.collection("friendRequests")
                .add(request)
                .await()

            Result.success("Friend request sent to ${friendUser.userName}.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptFriendRequest(currentUserId: String, requestId: String): Result<String> {
        return try {
            val requestDoc = firestore.collection("friendRequests").document(requestId)
            val requestSnapshot = requestDoc.get().await()

            if (!requestSnapshot.exists()) {
                return Result.failure(Exception("Friend request not found."))
            }

            val friendRequest = requestSnapshot.toObject(FriendRequest::class.java)
                ?: return Result.failure(Exception("Invalid friend request data."))

            val friendDocRef = firestore.collection("Users").document(friendRequest.fromUserId)
            val currentUserDocRef = firestore.collection("Users").document(currentUserId)

            firestore.runTransaction { transaction ->
                val currentUserSnapshot = transaction.get(currentUserDocRef)
                val friendSnapshot = transaction.get(friendDocRef)

                val currentUser = documentToUser(currentUserSnapshot)
                    ?: throw Exception("Current user not found.")
                val friendUser = documentToUser(friendSnapshot)
                    ?: throw Exception("Friend user not found.")

                val newFriend = Friend(uid = friendUser.uid, userName = friendUser.userName)
                val updatedCurrentUserFriends = currentUser.friend + newFriend

                transaction.update(currentUserDocRef, "friends", updatedCurrentUserFriends)

                val reciprocalFriend = Friend(uid = currentUser.uid, userName = currentUser.userName)
                val updatedFriendUserFriends = friendUser.friend + reciprocalFriend

                transaction.update(friendDocRef, "friends", updatedFriendUserFriends)

                transaction.delete(requestDoc)
            }.await()

            Result.success("Friend request accepted.")
        } catch (e: Exception) {
            Result.failure(e)
        }    }

    override suspend fun getFriendsBooks(userId: String): Result<List<Read>> {
        return try {
            val userSnapshot = firestore.collection("Users").document(userId).get().await()

            if (!userSnapshot.exists()) {
                return Result.failure(Exception("User not found."))
            }

            val user = documentToUser(userSnapshot)
                ?: return Result.failure(Exception("Invalid user data."))

            val books = user.friend.flatMap { friend ->
                val friendSnapshot = firestore.collection("Users").document(friend.uid).get().await()
                val friendUser = documentToUser(friendSnapshot)
                friendUser?.read ?: emptyList()
            }

            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }    }
    private fun documentToUser(snapshot: DocumentSnapshot): User? {
        return snapshot.toObject(User::class.java)
    }
}
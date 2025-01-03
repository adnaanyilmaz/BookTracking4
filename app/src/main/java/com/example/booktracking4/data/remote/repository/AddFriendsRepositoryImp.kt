package com.example.booktracking4.data.remote.repository

import android.util.Log
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.Friends
import com.example.booktracking4.data.remote.user.FriendsRequest
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddFriendsRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AddFriendsRepository {


    override suspend fun searchFriendByUsername(userName: String): Result<List<User>> {
        return try {
            val querySnapshot =
                firestore.collection("Users").whereEqualTo("userName", userName).get().await()

            val users = querySnapshot.documents.mapNotNull { documentToUser(it) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private val usersCollection = firestore.collection("Users")

    override suspend fun sendFriendRequest(
        senderUid: String,
        receiverUserName: String,
    ): Result<String> {
        return try {
            // Retrieve sender's information
            val senderSnapshot = usersCollection.document(senderUid).get().await()
            val sender = senderSnapshot.toObject<User>()
                ?: return Result.failure(Exception("Sender not found"))

            // Find the receiver by username
            val receiverSnapshot = usersCollection
                .whereEqualTo("userName", receiverUserName)
                .get()
                .await()

            if (receiverSnapshot.isEmpty) {
                return Result.failure(Exception("Receiver not found"))
            }

            val receiverDocument = receiverSnapshot.documents.first()
            val receiver = receiverDocument.toObject<User>()
                ?: return Result.failure(Exception("Receiver not found"))

            // Create a FriendsRequest object
            val friendRequest = FriendsRequest(
                uid = sender.uid,
                userName = sender.userName,
                email = sender.email
            )

            // Update the receiver's friendsRequest list
            val updatedFriendsRequest = receiver.friendsRequest.toMutableList().apply {
                add(friendRequest)
            }

            usersCollection.document(receiverDocument.id)
                .update("friendsRequest", updatedFriendsRequest)
                .await()

            Result.success("Sended Friend request")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptFriendRequest(
        receiverUid: String,
        senderUserName: String,
    ): Resource<String> {
        return try {
            // Kullanıcı dökümanlarını uid ve userName ile al
            val receiverQuery = usersCollection.whereEqualTo("uid", receiverUid).get().await()
            val senderQuery = usersCollection.whereEqualTo("userName", senderUserName).get().await()

            val receiverSnapshot = receiverQuery.documents.firstOrNull()
            val senderSnapshot = senderQuery.documents.firstOrNull()

            if (receiverSnapshot != null && senderSnapshot != null) {
                val receiver = receiverSnapshot.toObject(User::class.java)
                val sender = senderSnapshot.toObject(User::class.java)

                if (receiver != null && sender != null) {
                    // sender'ı receiver'ın `friendsRequest` listesinden kaldır
                    val updatedRequests =
                        receiver.friendsRequest.filterNot { it.userName == senderUserName }

                    // receiver'ın `friend` listesine sender'ı ekle
                    val updatedReceiverFriends = receiver.friend + Friends(
                        userName = sender.userName,
                        uid = sender.uid,
                        email = sender.email,
                    )

                    // sender'ın `friend` listesine receiver'ı ekle
                    val updatedSenderFriends = sender.friend + Friends(
                        userName = receiver.userName,
                        uid = receiver.uid,
                        userImage = sender.email,
                    )

                    // Güncellenmiş verileri Firestore'a yaz
                    receiverSnapshot.reference.update(
                        mapOf(
                            "friendsRequest" to updatedRequests,
                            "friend" to updatedReceiverFriends
                        )
                    ).await()

                    senderSnapshot.reference.update(
                        mapOf(
                            "friend" to updatedSenderFriends
                        )
                    ).await()
                }
            }
            Resource.Success("Friends Request Accepted")
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }


    override suspend fun rejectFriendRequest(
        receiverUid: String,
        senderUserName: String,
    ): Resource<String> {
        return try {
            // receiver'ın dökümanını uid ile al
            val receiverQuery = usersCollection.whereEqualTo("uid", receiverUid).get().await()
            val receiverSnapshot = receiverQuery.documents.firstOrNull()

            if (receiverSnapshot != null) {
                val receiver = receiverSnapshot.toObject(User::class.java)

                if (receiver != null) {
                    // sender'ı receiver'ın `friendsRequest` listesinden kaldır
                    val updatedRequests =
                        receiver.friendsRequest.filterNot { it.userName == senderUserName }

                    // Güncellenmiş `friendsRequest` listesini güncelle
                    receiverSnapshot.reference.update("friendsRequest", updatedRequests).await()
                }
            }
            Resource.Success("Friends Request Rejected")
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getFriendsList(uid: String): Resource<List<Friends>> {
        return try {
            // Kullanıcının dökümanını UID ile al
            val userQuery = usersCollection.whereEqualTo("uid", uid).get().await()
            val userSnapshot = userQuery.documents.firstOrNull()

            if (userSnapshot != null) {
                val user = userSnapshot.toObject(User::class.java)
                Resource.Success(user?.friend ?: emptyList())
            } else {
                Resource.Success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("GetFriendList", "Error: ${e.message}")
            Resource.Error(e.message ?: "getFriendsList An unknown error occurred.")
        }
    }

    override suspend fun getFriendsRequests(uid: String): Resource<List<FriendsRequest>> {
        return try {
            // Kullanıcının dökümanını UID ile al
            val userQuery = usersCollection.whereEqualTo("uid", uid).get().await()
            val userSnapshot = userQuery.documents.firstOrNull()

            if (userSnapshot != null) {
                val user = userSnapshot.toObject(User::class.java)
                Resource.Success(user?.friendsRequest ?: emptyList())
            } else {
                Resource.Success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("GetFriendRequests", "Error: ${e.message}")
            Resource.Error(e.message ?: "getFriendsRequests An unknown error occurred.")
        }
    }

    override suspend fun isUserInFriendsList(
        currentUserUid: String,
        friendUid: String
    ): Boolean {
        try {
            // Kullanıcının Firestore'daki belgesini al
            val userSnapshot = firestore.collection("users")
                .document(currentUserUid)
                .get()
                .await()

            // Kullanıcı belgesini User sınıfına dönüştür
            val user = userSnapshot.toObject(User::class.java)

            // Friends listesinde friendUid olup olmadığını kontrol et
            return user?.friend?.any { it.uid == friendUid } == true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }    }


    private fun documentToUser(snapshot: DocumentSnapshot): User? {
        return snapshot.toObject(User::class.java)
    }
}
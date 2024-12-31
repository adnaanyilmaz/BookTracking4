package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.FriendRequest
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun sendFriendRequest(senderUid: String, receiverUsername: String): Resource<Unit> {
        return try {
            // Kullanıcı adından UID sorgulama
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("userName", receiverUsername)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.documents.isEmpty()) {
                return Resource.Error("Kullanıcı bulunamadı")
            }

            val receiverUid = querySnapshot.documents.first().id
            val receiverRef = firestore.collection("Users").document(receiverUid)

            // Kullanıcı mevcutsa arkadaşlık isteğini ekle
            receiverRef.update("friendRequests", FieldValue.arrayUnion(FriendRequest(senderUid)))
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Beklenmeyen bir hata oluştu")
        }
    }


    override suspend fun acceptFriendRequest(
        currentUid: String,
        senderUid: String
    ): Resource<Unit> {
        return try {
            val currentUserRef = firestore.collection("Users").document(currentUid)
            val senderUserRef = firestore.collection("Users").document(senderUid)

            firestore.runBatch { batch ->
                // Add to friends list
                batch.update(currentUserRef, "friend", FieldValue.arrayUnion(senderUid))
                batch.update(senderUserRef, "friend", FieldValue.arrayUnion(currentUid))

                // Remove from friendRequests
                batch.update(
                    currentUserRef,
                    "friendRequests",
                    FieldValue.arrayRemove(FriendRequest(senderUid))
                )
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }


    override suspend fun getFriendsBooks(userId: String): Result<List<Read>> {
        return try {
            val userSnapshot = firestore.collection("Users").document(userId).get().await()

            if (!userSnapshot.exists()) {
                return Result.failure(Exception("User not found."))
            }

            val user = documentToUser(userSnapshot)
                ?: return Result.failure(Exception("Invalid user data."))

            val books = user.friend.flatMap { friend ->
                val friendSnapshot =
                    firestore.collection("Users").document(friend.uid).get().await()
                val friendUser = documentToUser(friendSnapshot)
                friendUser?.read ?: emptyList()
            }

            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFriendRequests(currentUid: String): Resource<List<FriendRequest>> {
        return try {
            val userSnapshot = firestore.collection("Users").document(currentUid).get().await()

            if (!userSnapshot.exists()) {
                return Resource.Error("User does not exist")
            }

            val friendRequests =
                userSnapshot.toObject(User::class.java)?.friendRequests ?: emptyList()
            Resource.Success(friendRequests)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }


    override suspend fun rejectFriendRequest(
        currentUid: String,
        senderUid: String
    ): Resource<Unit> {
        return try {
            val currentUserRef = firestore.collection("Users").document(currentUid)
            currentUserRef.update(
                "friendRequests",
                FieldValue.arrayRemove(FriendRequest(senderUid))
            ).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }


    private fun documentToUser(snapshot: DocumentSnapshot): User? {
        return snapshot.toObject(User::class.java)
    }
}
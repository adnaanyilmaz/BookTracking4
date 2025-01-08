package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.UserCategories
import com.example.booktracking4.data.remote.user.WantToRead
import com.example.booktracking4.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserRepository {
    override suspend fun addUser(user: User): Resource<String> {
        return try {
            firestore.collection("Users")
                .document(user.uid)
                .set(user)
                .await()
            Resource.Success(user.userName)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }

    override suspend fun addBookToRead(
        userId: String,
        book: Read,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.read?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "read", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I read: ${e.message}")
        }
    }

    override suspend fun addBookToWantToRead(
        userId: String,
        book: WantToRead,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.wantToRead?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Will Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "wantToRead", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun addBookToCurrentlyReading(
        userId: String,
        book: CurrentlyReading,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.currentlyReading?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Will Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "currentlyReading", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun deleteUserBooks(userId: String, bookId: String): Resource<User> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)

            // Firestore'da kullanıcı bilgilerini al ve kitabı sil
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                if (currentUser == null) {
                    throw Exception("User not found")
                }

                // Kitabı kaldırma işlemleri
                val updatedCurrentlyReading =
                    currentUser.currentlyReading.filterNot { it.bookId == bookId }
                val updatedRead = currentUser.read.filterNot { it.bookId == bookId }
                val updatedWantToRead = currentUser.wantToRead.filterNot { it.bookId == bookId }

                // Eğer hiçbir listede kitap yoksa hata fırlat
                if (currentUser.currentlyReading.size == updatedCurrentlyReading.size &&
                    currentUser.read.size == updatedRead.size &&
                    currentUser.wantToRead.size == updatedWantToRead.size
                ) {
                    throw Exception("Book not found in any list")
                }

                // Kullanıcıyı güncelle
                val updatedUser = currentUser.copy(
                    currentlyReading = updatedCurrentlyReading,
                    read = updatedRead,
                    wantToRead = updatedWantToRead
                )
                transaction.set(userDocRef, updatedUser)
            }.await()

            // Başarıyla güncellenen kullanıcıyı döndür
            val updatedUser = firestore.collection("Users").document(userId).get().await()
                .toObject(User::class.java)
            if (updatedUser != null) {
                Resource.Success(updatedUser)
            } else {
                Resource.Error("Failed to retrieve updated user data after deletion")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to delete book: ${e.message}")
        }
    }


    override suspend fun getUserBooks(userId: String): Resource<User> {
        return try {
            val userDoc = firestore.collection("Users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)

            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to fetch user books: ${e.message}")
        }
    }

    override suspend fun getUserName(uid: String): Resource<String> {
        return try {
            val snapshot = firestore.collection("Users")
                .document(uid)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                Resource.Success(user.userName)
            } else {
                Resource.Error("User not found with uid: $uid")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun checkUserName(userName: String): Boolean {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("Users")
            .whereEqualTo("userName", userName)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    override suspend fun addUserCategories(
        userId: String,
        category: UserCategories,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedCategories = currentUser?.userCategory?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedCategories.any { it.userCategoryName == category.userCategoryName }) {
                    throw Exception("Exist")
                }

                updatedCategories.add(category)
                transaction.update(userDocRef, "userCategory", updatedCategories)
            }.await()

            Resource.Success(category.userCategoryName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun getUserCategories(userId: String): Resource<List<UserCategories>> {
        return try {
            val snapshot = firestore.collection("Users")
                .document(userId)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                Resource.Success(user.userCategory)
            } else {
                Resource.Error("User not found with uid: $userId")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }


}
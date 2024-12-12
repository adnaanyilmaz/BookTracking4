package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.ReadNow
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.WhatIRead
import com.example.booktracking4.data.remote.user.WhatIWillRead
import com.example.booktracking4.domain.model.ui_model.search_model.Book
import com.example.booktracking4.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserRepository{
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

    override suspend fun addBookToWhatIRead(
        userId: String,
        book: WhatIRead
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.whatIRead?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "whatIRead", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I read: ${e.message}")
        }
    }

    override suspend fun addBookToWhatIWillRead(
        userId: String,
        book: WhatIWillRead
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.whatIWillRead?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Will Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "whatIWillRead", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun addBookToReadNow(
        userId: String,
        book: ReadNow
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.readNow?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in the 'What I Will Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "readNow", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
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

    override suspend fun getUserForBooks(bookId: String): Resource<List<Book>> {
        TODO("Not yet implemented")
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



}
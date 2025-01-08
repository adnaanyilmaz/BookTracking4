package com.example.booktracking4.data.remote.repository

import android.util.Log
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class NotesRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : NotesRepository {

    private val usersCollection = "Users" // Firestore'daki kullanıcı koleksiyonunun adı

    override fun getNotes(): Flow<List<BookNote>> = flow {
        try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("Kullanıcı giriş yapmamış")
            val userDocRef = firestore.collection(usersCollection).document(currentUserUid)

            val snapshot = userDocRef.get().await()
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                emit(user.notes)
            } else {
                emit(emptyList()) // Eğer kullanıcı bulunamazsa boş liste döner
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // Hata durumunda boş liste döner
        }
    }

    override suspend fun getNoteById(id: Int?): BookNote? {
        try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("Kullanıcı giriş yapmamış")
            val userDocRef = firestore.collection(usersCollection).document(currentUserUid)

            val snapshot = userDocRef.get().await()
            val user = snapshot.toObject(User::class.java)

            return user?.notes?.find { it.id == id }
        } catch (e: Exception) {
            e.printStackTrace()
            return null // Hata durumunda null döner
        }
    }

    override suspend fun insertNote(note: BookNote) {
        try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("Kullanıcı giriş yapmamış")
            val userDocRef = firestore.collection(usersCollection).document(currentUserUid)

            val userSnapshot = userDocRef.get().await()
            val user = userSnapshot.toObject(User::class.java)

            if (user != null) {
                val updatedNotes = user.notes.toMutableList().apply {
                    add(note)
                }

                userDocRef.update("notes", updatedNotes).await()
            } else {
                throw Exception("Kullanıcı dokümanı bulunamadı")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun deleteNote(note: BookNote) {
        try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("Kullanıcı giriş yapmamış")
            val userDocRef = firestore.collection(usersCollection).document(currentUserUid)

            val userSnapshot = userDocRef.get().await()
            val user = userSnapshot.toObject(User::class.java)

            if (user != null) {
                val updatedNotes = user.notes.toMutableList().apply {
                    removeIf { it.id == note.id }
                }

                userDocRef.update("notes", updatedNotes).await()
            } else {
                throw Exception("Kullanıcı dokümanı bulunamadı")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun updateNote(note: BookNote) {
        try {
            val currentUserUid = auth.currentUser?.uid ?: throw Exception("Kullanıcı giriş yapmamış")
            val userDocRef = firestore.collection(usersCollection).document(currentUserUid)

            val userSnapshot = userDocRef.get().await()
            val user = userSnapshot.toObject(User::class.java)

            if (user != null) {
                val updatedNotes = user.notes.toMutableList().apply {
                    val index = indexOfFirst { it.id == note.id }
                    if (index != -1) {
                        set(index, note) // Mevcut notu güncelle
                    }
                }

                userDocRef.update("notes", updatedNotes).await()
                Log.d("firestore",updatedNotes.toString())
            } else {
                throw Exception("Kullanıcı dokümanı bulunamadı")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}


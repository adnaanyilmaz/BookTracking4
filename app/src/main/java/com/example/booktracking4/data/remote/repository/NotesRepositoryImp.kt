package com.example.booktracking4.data.remote.repository

import android.util.Log
import com.example.booktracking4.data.data_resourse.NoteDao
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.NotesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NotesRepositoryImp @Inject constructor(
    private val bookNoteDao: NoteDao,
    private val firestore: FirebaseFirestore
): NotesRepository {
    override suspend fun getAllNotesFromRoom(): Flow<List<BookNote>> {
        return bookNoteDao.getNotes()
    }

    override suspend fun syncNotesWithFirebase(uid: String, notes: List<BookNote>) {
        val userNotesMap = mapOf(
            "notes" to notes.map { bookNote ->
                mapOf(
                    "bookName" to (bookNote.bookName ?: ""),
                    "title" to bookNote.title,
                    "content" to bookNote.content,
                    "timestamp" to bookNote.timestamp,
                    "page" to bookNote.page,
                    "isFavorite" to bookNote.isFavorite
                )
            }
        )

        firestore.collection("Users")
            .document(uid)
            .set(userNotesMap, SetOptions.merge()) // Mevcut verileri koruyarak günceller
            .addOnSuccessListener {
                Log.d("FirebaseSync", "Notes successfully updated in User object")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseSync", "Error updating notes in User object: ${e.message}")
            }
    }


}
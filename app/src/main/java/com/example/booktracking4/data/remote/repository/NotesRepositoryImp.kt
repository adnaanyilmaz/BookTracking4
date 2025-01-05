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
    private val firestore: FirebaseFirestore,
): NotesRepository {
    override suspend fun getAllNotesFromRoom(uid: String): Flow<List<BookNote>> {
        return bookNoteDao.getNotes(uid)
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
                    "isFavorite" to bookNote.isFavorite,
                    "userId" to bookNote.userId,
                    "id" to bookNote.id

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

    // Firebase'den kullanıcıya özel notları yükle
    override suspend fun fetchNotesFromFirebase(uid: String): List<BookNote> {
        val result = mutableListOf<BookNote>()
        firestore.collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val notes = document.get("notes") as? List<Map<String, Any>> ?: emptyList()
                result.addAll(
                    notes.map { note ->
                        BookNote(
                            bookName = note["bookName"] as? String ?: "",
                            title = note["title"] as String,
                            content = note["content"] as String,
                            timestamp = (note["timestamp"] as? Long) ?: 0L,
                            page = note["page"] as String,
                            isFavorite = note["isFavorite"] as? Boolean ?: false,
                            id = note["id"] as? Int,
                            userId =note["userId"] as? String ?: "",
                        )
                    }
                )
                Log.d("FirebaseSync", "Notes fetched successfully for user: $uid")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseSync", "Error fetching notes for user: ${e.message}")
            }
        return result
    }
    override suspend fun syncRoomWithFirebase(uid: String) {
        val notesFromFirebase = fetchNotesFromFirebase(uid)
        notesFromFirebase.forEach { bookNote ->
            bookNoteDao.insertNote(bookNote.copy(userId = uid)) // Room'da kullanıcıya özel alan eklenir
        }
        Log.d("FirebaseSync", "Room database synced with Firebase for user: $uid")
    }
}
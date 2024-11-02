package com.example.booktracking4.data.data_resourse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booktracking4.domain.model.room.BookNote

@Database(
    entities = [BookNote::class],
    version = 1
)
abstract class BookNoteDatabase: RoomDatabase() {
    abstract val bookNoteDao: NoteDao

    companion object{
        const val DATABASE_NAME="book_note_db"
    }
}
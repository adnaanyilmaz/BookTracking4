package com.example.booktracking4.data.data_resourse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.booktracking4.domain.model.room.BookNote

@Database(
    entities = [BookNote::class],
    version = 3
)
abstract class BookNoteDatabase: RoomDatabase() {
    abstract fun bookNoteDao(): NoteDao

    companion object{
        const val DATABASE_NAME="book_note_db"

    }

}
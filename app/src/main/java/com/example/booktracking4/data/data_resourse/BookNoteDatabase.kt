package com.example.booktracking4.data.data_resourse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.booktracking4.domain.model.room.BookNote

@Database(
    entities = [BookNote::class],
    version = 2
)
abstract class BookNoteDatabase: RoomDatabase() {
    abstract val bookNoteDao: NoteDao

    companion object{
        const val DATABASE_NAME="book_note_db"
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Şema değişikliği için SQL komutları
                database.execSQL("ALTER TABLE BookNote ADD COLUMN bookName TEXT")
            }
        }
    }

}
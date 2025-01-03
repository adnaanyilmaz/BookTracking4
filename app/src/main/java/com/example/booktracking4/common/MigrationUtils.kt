package com.example.booktracking4.common

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationUtils {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Şema değişikliği için SQL komutları
            database.execSQL("ALTER TABLE BookNote ADD COLUMN bookName TEXT")
        }
    }
}
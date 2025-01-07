package com.example.booktracking4.data.data_resourse

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.booktracking4.domain.model.room.BookNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM booknote WHERE userId = :userId")
    fun getNotes(userId: String): Flow<List<BookNote>>

    @Query("SELECT * FROM booknote WHERE id= :id")
    suspend fun getNoteById(id: Int?): BookNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: BookNote)

    @Delete
    suspend fun deteleNote(note: BookNote)

    @Update
    suspend fun updateNote(note: BookNote)
}
package com.example.booktracking4.di

import android.app.Application
import androidx.room.Room
import com.example.booktracking4.common.Constants
import com.example.booktracking4.data.data_resourse.BookNoteDatabase
import com.example.booktracking4.data.data_resourse.NoteDao
import com.example.booktracking4.data.data_resourse.repository.BookNoteRepositoryImp
import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.remote.repository.BookRepositoryImpl
import com.example.booktracking4.data.remote.repository.NotesRepositoryImp
import com.example.booktracking4.domain.repository.BookNoteRepository
import com.example.booktracking4.domain.repository.BookRepository
import com.example.booktracking4.domain.repository.NotesRepository
import com.example.booktracking4.domain.usecase.note_use_cases.AddBookNoteUseCase
import com.example.booktracking4.domain.usecase.note_use_cases.BookNoteUseCases
import com.example.booktracking4.domain.usecase.note_use_cases.DeleteBookNoteUseCase
import com.example.booktracking4.domain.usecase.note_use_cases.EditNoteUseCase
import com.example.booktracking4.domain.usecase.note_use_cases.GetBookNoteUseCase
import com.example.booktracking4.domain.usecase.note_use_cases.UpdateFavoriteStatusUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoogleBooksApi(): GoogleBooksApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(api: GoogleBooksApi): BookRepository {
        return BookRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBookNoteDatabase(app: Application): BookNoteDatabase {
        return Room.databaseBuilder(
            app, BookNoteDatabase::class.java,
            BookNoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: BookNoteDatabase): NoteDao {
        return database.bookNoteDao()
    }

    @Provides
    @Singleton
    fun provideBookNoteRepository(db: BookNoteDatabase,auth: FirebaseAuth): BookNoteRepository {
        return BookNoteRepositoryImp(db.bookNoteDao(),auth)
    }

    @Provides
    @Singleton
    fun provideBookNoteUseCases(repository: BookNoteRepository): BookNoteUseCases {
        return BookNoteUseCases(
            GetBookNoteUseCase(repository),
            DeleteBookNoteUseCase(repository),
            AddBookNoteUseCase(repository),
            EditNoteUseCase(repository),
            UpdateFavoriteStatusUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideNotesRepository(
        bookNoteDao: NoteDao,
        firestore: FirebaseFirestore,
    ): NotesRepository {
        return NotesRepositoryImp(bookNoteDao, firestore)
    }
}

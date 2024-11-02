package com.example.booktracking4.di

import android.app.Application
import androidx.room.Room
import com.example.booktracking4.common.Constants
import com.example.booktracking4.data.data_resourse.BookNoteDatabase
import com.example.booktracking4.data.data_resourse.repository.BookNoteRepositoryImp
import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.remote.repository.BookRepositoryImpl
import com.example.booktracking4.domain.repository.BookNoteRepository
import com.example.booktracking4.domain.repository.BookRepository
import com.example.booktracking4.domain.usecase.AddBookNoteUseCase
import com.example.booktracking4.domain.usecase.BookNoteUseCases
import com.example.booktracking4.domain.usecase.DeleteBookNoteUseCase
import com.example.booktracking4.domain.usecase.GetBookNoteUseCase
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
    fun provideBookNoteRepository(db: BookNoteDatabase): BookNoteRepository{
        return BookNoteRepositoryImp(db.bookNoteDao)
    }

    @Provides
    @Singleton
    fun provideBookNoteUseCases(repository: BookNoteRepository): BookNoteUseCases{
        return BookNoteUseCases(
            GetBookNoteUseCase(repository),
            DeleteBookNoteUseCase(repository),
            AddBookNoteUseCase(repository)
        )
    }
}
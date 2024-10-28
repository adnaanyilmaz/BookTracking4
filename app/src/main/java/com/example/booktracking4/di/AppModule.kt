package com.example.booktracking4.di

import com.example.booktracking4.common.Constants
import com.example.booktracking4.data.remote.GoogleBooksApi
import com.example.booktracking4.data.remote.repository.BookRepositoryImpl
import com.example.booktracking4.domain.repository.BookRepository
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
    fun provideGoogleBooksApi():GoogleBooksApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)

    }
    @Provides
    @Singleton
    fun provideBookRepository(api: GoogleBooksApi):BookRepository{
        return BookRepositoryImpl(api)
    }
}
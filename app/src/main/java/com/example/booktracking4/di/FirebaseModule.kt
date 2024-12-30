package com.example.booktracking4.di

import com.example.booktracking4.data.remote.repository.AddFriendsRepositoryImp
import com.example.booktracking4.data.remote.repository.UserRepositoryImpl
import com.example.booktracking4.data.repository.AuthRepository
import com.example.booktracking4.domain.repository.AddFriendsRepository
import com.example.booktracking4.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepository(auth)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideUserRepository(fireStore: FirebaseFirestore): UserRepository =
        UserRepositoryImpl(fireStore)

    @Provides
    @Singleton
    fun provideAddFriendsRepository(fireStore: FirebaseFirestore): AddFriendsRepository =
        AddFriendsRepositoryImp(fireStore)

}
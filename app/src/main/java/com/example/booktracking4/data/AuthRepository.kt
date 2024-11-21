package com.example.booktracking4.data

import com.example.booktracking4.common.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun signUp(email: String, password: String): Resource<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid // opsiyonel
            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid
            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun signOut() = auth.signOut()

}
package com.example.booktracking4.data.repository

import com.example.booktracking4.common.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun signUp(email: String, password: String): Resource<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid // opsiyonel
            // Username kontrolü

            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
//    // Kullanıcı adının mevcut olup olmadığını kontrol eden fonksiyon
//    private suspend fun checkUserNameExists(userName: String): Boolean {
//        // Burada Firebase veritabanında kullanıcı adını kontrol edin
//        // Örneğin, Firestore kullanıyorsanız:
//        val snapshot = FirebaseFirestore.getInstance()
//            .collection("users")
//            .whereEqualTo("userName", userName)
//            .get()
//            .await()
//
//        return !snapshot.isEmpty // Eğer snapshot boş değilse, kullanıcı adı zaten mevcut demektir
//    }

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

    fun getUserId(): String = auth.currentUser?.uid.orEmpty()


    fun signOut() = auth.signOut()

}
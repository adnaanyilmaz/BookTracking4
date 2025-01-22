package com.example.booktracking4.data.remote.repository

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.UserCategories
import com.example.booktracking4.data.remote.user.WantToRead
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserRepository {
    override suspend fun addUser(user: User): Resource<String> {
        return try {
            firestore.collection("Users")
                .document(user.uid)
                .set(user)
                .await()
            Resource.Success(user.userName)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }

    override suspend fun addBookToRead(
        userId: String,
        book: Read,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.read?.toMutableList() ?: mutableListOf()
                val wantToReadBooks = currentUser?.wantToRead ?: emptyList()
                val currentlyReadingBooks = currentUser?.currentlyReading ?: emptyList()

                // Kitap diğer listelerde veya mevcut listede zaten varsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId } ||
                    wantToReadBooks.any { it.bookId == book.bookId } ||
                    currentlyReadingBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in another list or in 'What I Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "read", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I read: ${e.message}")
        }
    }

    override suspend fun addBookToWantToRead(
        userId: String,
        book: WantToRead,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.wantToRead?.toMutableList() ?: mutableListOf()
                val readBooks = currentUser?.read ?: emptyList()
                val currentlyReadingBooks = currentUser?.currentlyReading ?: emptyList()

                // Kitap diğer listelerde veya mevcut listede zaten varsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId } ||
                    readBooks.any { it.bookId == book.bookId } ||
                    currentlyReadingBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in another list or in 'What I Will Read' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "wantToRead", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun addBookToCurrentlyReading(
        userId: String,
        book: CurrentlyReading,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedBooks = currentUser?.currentlyReading?.toMutableList() ?: mutableListOf()
                val readBooks = currentUser?.read ?: emptyList()
                val wantToReadBooks = currentUser?.wantToRead ?: emptyList()

                // Kitap diğer listelerde veya mevcut listede zaten varsa işlem yapılmaz
                if (updatedBooks.any { it.bookId == book.bookId } ||
                    readBooks.any { it.bookId == book.bookId } ||
                    wantToReadBooks.any { it.bookId == book.bookId }) {
                    throw Exception("Book is already in another list or in 'Currently Reading' list")
                }

                updatedBooks.add(book)
                transaction.update(userDocRef, "currentlyReading", updatedBooks)
            }.await()

            Resource.Success(book.bookName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to currently reading: ${e.message}")
        }
    }


    override suspend fun deleteUserBooks(userId: String, bookId: String): Resource<User> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)

            // Firestore'da kullanıcı bilgilerini al ve kitabı sil
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                if (currentUser == null) {
                    throw Exception("User not found")
                }

                // Kitabı kaldırma işlemleri
                val updatedCurrentlyReading =
                    currentUser.currentlyReading.filterNot { it.bookId == bookId }
                val updatedRead = currentUser.read.filterNot { it.bookId == bookId }
                val updatedWantToRead = currentUser.wantToRead.filterNot { it.bookId == bookId }

                // Eğer hiçbir listede kitap yoksa hata fırlat
                if (currentUser.currentlyReading.size == updatedCurrentlyReading.size &&
                    currentUser.read.size == updatedRead.size &&
                    currentUser.wantToRead.size == updatedWantToRead.size
                ) {
                    throw Exception("Book not found in any list")
                }

                // Kullanıcıyı güncelle
                val updatedUser = currentUser.copy(
                    currentlyReading = updatedCurrentlyReading,
                    read = updatedRead,
                    wantToRead = updatedWantToRead
                )
                transaction.set(userDocRef, updatedUser)
            }.await()

            // Başarıyla güncellenen kullanıcıyı döndür
            val updatedUser = firestore.collection("Users").document(userId).get().await()
                .toObject(User::class.java)
            if (updatedUser != null) {
                Resource.Success(updatedUser)
            } else {
                Resource.Error("Failed to retrieve updated user data after deletion")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to delete book: ${e.message}")
        }
    }


    override suspend fun getUserBooks(userId: String): Resource<User> {
        return try {
            val userDoc = firestore.collection("Users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)

            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to fetch user books: ${e.message}")
        }
    }

    override suspend fun getUserName(uid: String): Resource<String> {
        return try {
            val snapshot = firestore.collection("Users")
                .document(uid)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                Resource.Success(user.userName)
            } else {
                Resource.Error("User not found with uid: $uid")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun checkUserName(userName: String): Boolean {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("Users")
            .whereEqualTo("userName", userName)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    override suspend fun addUserCategories(
        userId: String,
        category: UserCategories,
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val currentUser = snapshot.toObject(User::class.java)

                val updatedCategories =
                    currentUser?.userCategory?.toMutableList() ?: mutableListOf()

                // Kitap zaten mevcutsa işlem yapılmaz
                if (updatedCategories.any { it.userCategoryName == category.userCategoryName }) {
                    throw Exception("Exist")
                }

                updatedCategories.add(category)
                transaction.update(userDocRef, "userCategory", updatedCategories)
            }.await()

            Resource.Success(category.userCategoryName)
        } catch (e: Exception) {
            Resource.Error("Failed to add book to what I will read: ${e.message}")
        }
    }

    override suspend fun getUserCategories(userId: String): Resource<List<UserCategories>> {
        return try {
            val snapshot = firestore.collection("Users")
                .document(userId)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                Resource.Success(user.userCategory)
            } else {
                Resource.Error("User not found with uid: $userId")
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun updateIsFavorite(
        userId: String,
        bookId: String,
        newIsFavoriteStatus: Boolean
    ): Result<Unit> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)

            // Firestore işlem bloğu ile güncelleme
            firestore.runTransaction { transaction ->
                // Kullanıcı verisini getir
                val snapshot = transaction.get(userDocRef)
                val user = snapshot.toObject(User::class.java)
                    ?: throw IllegalArgumentException("User not found.")

                // currentlyReading listesini güncelle
                val updatedRead = user.read.map { book ->
                    if (book.bookId == bookId) {
                        book.copy(isFavorite = newIsFavoriteStatus) // Yeni isFavorite durumu
                    } else {
                        book
                    }
                }

                // Güncellenmiş listeyi Firestore'a yaz
                transaction.update(userDocRef, "read", updatedRead)
            }.await() // Asenkron işlemi bekle
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteBooks(userId: String): Resource<List<Read>> {
        return try {
            val userDocRef = firestore.collection("Users").document(userId)

            // Kullanıcı verisini çek
            val snapshot = userDocRef.get().await()
            val user = snapshot.toObject(User::class.java)
                ?: throw IllegalArgumentException("User not found.")

            // read listesini filtrele
            val favoriteBooks = user.read.filter { it.isFavorite }

            Resource.Success(favoriteBooks)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getFriendsPublicNotes(userId: String): Resource<List<Pair<String, BookNote>>> {
        try {
            // Kullanıcının arkadaşlarını Firestore'dan al
            val userSnapshot = firestore.collection("Users")
                .document(userId)
                .get()
                .await()
            val userFriends = userSnapshot.toObject(User::class.java)?.friend ?: emptyList()

            // Tüm arkadaşların public notlarını topla
            val publicNotesWithUsername = mutableListOf<Pair<String, BookNote>>()
            for (friend in userFriends) {
                val friendSnapshot = firestore.collection("Users")
                    .document(friend.uid)
                    .get()
                    .await()
                val friendData = friendSnapshot.toObject(User::class.java)
                val friendUsername = friendData?.userName ?: "Unknown"
                val friendNotes = friendData?.notes ?: emptyList()

                // Sadece public (status = false) notları ekle ve username ile eşleştir
                publicNotesWithUsername.addAll(friendNotes.filter { !it.status }
                    .map { friendUsername to it })
            }
            return Resource.Success(publicNotesWithUsername)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(e.message, emptyList())
        }
    }

    override suspend fun isAdmin(userId: String): Flow<Boolean> = flow {
        val snapshot = firestore.collection("Users").document(userId).get().await()
        val isAdmin = snapshot.getBoolean("admin") ?: false
        emit(isAdmin)
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        return try {

            // Firestore'daki "users" koleksiyonundaki verileri getir
            val snapshot = firestore.collection("Users").get().await()

            // Gelen belgeleri User nesnesine dönüştür
            val users = snapshot.documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }

            // Başarılı durum döndür
            Resource.Success(users)
        } catch (e: Exception) {
            // Hata durumunda mesaj ve boş veri döndür
            Resource.Error("Kullanıcılar yüklenirken bir hata oluştu: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteUser(uid: String): Resource<Boolean> {
        return try {
            // Firestore'daki "users" koleksiyonunda belirtilen UID'yi sil
            firestore.collection("Users")
                .document(uid)
                .delete()
                .await()

            // Başarılı durum döndür
            Resource.Success(true)
        } catch (e: Exception) {
            // Hata durumunda mesaj ve false döndür
            Resource.Error("Kullanıcı silinirken bir hata oluştu: ${e.localizedMessage}", false)
        }
    }

    override suspend fun getAllPublicNotes(): Resource<List<Pair<String, BookNote>>> {
        return try {
            // Tüm kullanıcıları "Users" koleksiyonundan al
            val usersSnapshot = firestore.collection("Users").get().await()
            val publicNotesWithUsername = mutableListOf<Pair<String, BookNote>>()

            // Her kullanıcıyı işle
            for (document in usersSnapshot.documents) {
                val userData = document.toObject(User::class.java)
                val userName = userData?.userName ?: "Unknown"
                val userNotes = userData?.notes ?: emptyList()

                // Public (status = false) olan notları listeye ekle
                publicNotesWithUsername.addAll(
                    userNotes.filter { !it.status }
                        .map { userName to it }
                )
            }

            // Başarıyla sonuçlanırsa listeyi döndür
            Resource.Success(publicNotesWithUsername)
        } catch (e: Exception) {
            e.printStackTrace()
            // Hata durumunda boş bir liste ve hata mesajı döndür
            Resource.Error("Public notlar alınırken bir hata oluştu: ${e.localizedMessage}", emptyList())
        }    }

    override suspend fun deleteUserPublicNote(
        note: BookNote
    ): Resource<Boolean> {
        return try {
            // Tüm kullanıcıları kontrol et ve notu sil
            val usersSnapshot = firestore.collection("Users").get().await()

            for (userDocument in usersSnapshot.documents) {
                val userData = userDocument.toObject(User::class.java)

                if (userData != null && userData.notes.any { it.id == note.id}) {
                    // Not bulundu, güncellenmiş listeyi oluştur
                    val updatedNotes = userData.notes.filter { it.id != note.id }

                    // Güncellenmiş listeyi ilgili kullanıcı dokümanına yaz
                    firestore.collection("Users")
                        .document(userDocument.id)
                        .update("notes", updatedNotes)
                        .await()

                    return Resource.Success(true) // Başarılı
                }
            }

            Resource.Error("Not bulunamadı", false) // Not hiçbir kullanıcıda yok
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error("Not silinirken bir hata oluştu: ${e.localizedMessage}", false)
        }
    }
}

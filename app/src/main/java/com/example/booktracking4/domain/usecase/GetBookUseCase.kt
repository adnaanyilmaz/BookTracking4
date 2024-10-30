package com.example.booktracking4.domain.usecase

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.dto.BookDto
import com.example.booktracking4.data.remote.dto.toBook
import com.example.booktracking4.data.remote.dto.toBooks
import com.example.booktracking4.domain.model.Book
import com.example.booktracking4.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(searchWord: String): Flow<Resource<List<Book>>> = flow {
        try {
            emit(Resource.Loading())
            val bookDto = repository.getBook(searchWord)
            val books = bookDto.toBooks() // Burada değişiklik yaptık
            emit(Resource.Success(books))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}


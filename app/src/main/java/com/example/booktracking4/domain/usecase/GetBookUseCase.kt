package com.example.booktracking4.domain.usecase

import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.dto.toBook
import com.example.booktracking4.domain.model.Book
import com.example.booktracking4.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
     operator fun invoke(): Flow<Resource<List<Book>>> = flow {
         try {
             emit(Resource.Loading())
             val book=repository.getBook(searchWord ="Suç ve ceza" ).map { it.toBook() }
             emit(Resource.Success(book))
         }catch (e:HttpException){
             emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
         }catch (e:IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
         }
     }
}
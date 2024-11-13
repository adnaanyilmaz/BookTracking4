package com.example.booktracking4.domain.usecase.book_detail_usecase

import coil.network.HttpException
import com.example.booktracking4.common.Resource
import com.example.booktracking4.data.remote.dto.detail_dto.toBook
import com.example.booktracking4.domain.model.retrofit.BookDetail
import com.example.booktracking4.domain.repository.BookRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    private val repository: BookRepository
){
    operator fun invoke(id: String): Flow<Resource<BookDetail>> = flow {
        try {
            emit(Resource.Loading())
            val bookDetailDto=repository.getBookDetail(id)
            val book=bookDetailDto.toBook()
            emit(Resource.Success(book))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
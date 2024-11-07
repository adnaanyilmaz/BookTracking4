package com.example.booktracking4.data.remote.dto.detail_dto

import com.example.booktracking4.data.remote.dto.search_dto.toBook
import com.example.booktracking4.domain.model.retrofit.Book
import com.example.booktracking4.domain.model.retrofit.BookDetail

data class BookDetailDto(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val layerInfo: LayerInfo,
    val saleInfo: SaleInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)
fun BookDetailDto.toBook(): BookDetail{
    return BookDetail(
        authors = volumeInfo.authors,
        categories = volumeInfo.categories,
        description = volumeInfo.description,
        imageLinks = volumeInfo.imageLinks,
        industryIdentifiers = volumeInfo.industryIdentifiers,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate,
        publisher = volumeInfo.publisher,
        ratingsCount = volumeInfo.ratingsCount,
        title = volumeInfo.title
    )
}
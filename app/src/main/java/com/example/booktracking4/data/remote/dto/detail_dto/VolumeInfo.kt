package com.example.booktracking4.data.remote.dto.detail_dto

import com.example.booktracking4.domain.model.retrofit.BookDetail

data class VolumeInfo(
    val allowAnonLogging: Boolean,
    val authors: List<String>,
    val averageRating: Int,
    val canonicalVolumeLink: String,
    val categories: List<String>,
    val contentVersion: String,
    val description: String,
    val dimensions: Dimensions,
    val imageLinks: ImageLinks,
    val industryIdentifiers: List<IndustryIdentifier>,
    val infoLink: String,
    val language: String,
    val maturityRating: String,
    val pageCount: Int,
    val panelizationSummary: PanelizationSummary,
    val previewLink: String,
    val printType: String,
    val printedPageCount: Int,
    val publishedDate: String,
    val publisher: String,
    val ratingsCount: Int,
    val readingModes: ReadingModes,
    val subtitle: String,
    val title: String
)
//fun VolumeInfo.toBook(): BookDetail {
//    return BookDetail(
//        authors = authors,
//        categories = categories,
//        description = description,
//        imageLinks = imageLinks,
//        industryIdentifiers = industryIdentifiers,
//        pageCount = pageCount,
//        publishedDate = publishedDate,
//        publisher = publisher,
//        ratingsCount = ratingsCount,
//        title = title
//    )
//}

package com.example.booktracking4.data.remote.dto.search_dto

data class VolumeInfo(
    val allowAnonLogging: Boolean,
    val authors: List<String>,
    val averageRating: Int,
    val canonicalVolumeLink: String,
    val categories: List<String>,
    val contentVersion: String,
    val description: String,
    val imageLinks: ImageLinks,
    val industryIdentifiers: List<İndustryIdentifier>,
    val infoLink: String,
    val language: String,
    val maturityRating: String,
    val pageCount: Int,
    val panelizationSummary: PanelizationSummary,
    val previewLink: String,
    val printType: String,
    val publishedDate: String,
    val publisher: String,
    val ratingsCount: Int,
    val readingModes: ReadingModes,
    val subtitle: String,
    val title: String
)

//fun VolumeInfo.toBook(): Book {
//    return Book(
//        authors = authors,
//        categories = categories,
//        imageLinks = imageLinks,
//        title = title,
//    )
//}
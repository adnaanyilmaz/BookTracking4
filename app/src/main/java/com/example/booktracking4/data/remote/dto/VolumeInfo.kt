package com.example.booktracking4.data.remote.dto

import com.example.booktracking4.domain.model.Book

data class VolumeInfo(
    val allowAnonLogging: Boolean,
    val authors: List<String>,
    val averageRating: Int,
    val canonicalVolumeLink: String,
    val categories: List<String>,
    val contentVersion: String,
    val description: String,
    val imageLinks: İmageLinks,
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
fun VolumeInfo.toBook():Book{
    return Book(
        allowAnonLogging = allowAnonLogging,
        authors = authors,
        averageRating = averageRating,
        categories = categories,
        contentVersion = contentVersion,
        description = description,
        imageLinks = imageLinks,
        industryIdentifiers = industryIdentifiers,
        infoLink = infoLink,
        language = language,
        maturityRating =maturityRating ,
        pageCount = pageCount,
        panelizationSummary =panelizationSummary,
        previewLink = previewLink,
        printType = printType,
        publishedDate = publishedDate,
        publisher = publisher,
        ratingsCount = ratingsCount,
        readingModes = readingModes,
        subtitle =subtitle,
        title = title,
    )
}
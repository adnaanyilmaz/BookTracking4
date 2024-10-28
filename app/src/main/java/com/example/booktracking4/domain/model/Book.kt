package com.example.booktracking4.domain.model

import com.example.booktracking4.data.remote.dto.PanelizationSummary
import com.example.booktracking4.data.remote.dto.ReadingModes
import com.example.booktracking4.data.remote.dto.İmageLinks
import com.example.booktracking4.data.remote.dto.İndustryIdentifier

data class Book(
    val allowAnonLogging: Boolean,
    val authors: List<String>,
    val averageRating: Int,
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
) {
}
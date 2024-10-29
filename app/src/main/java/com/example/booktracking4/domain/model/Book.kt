package com.example.booktracking4.domain.model

import com.example.booktracking4.data.remote.dto.PanelizationSummary
import com.example.booktracking4.data.remote.dto.ReadingModes
import com.example.booktracking4.data.remote.dto.İmageLinks
import com.example.booktracking4.data.remote.dto.İndustryIdentifier

data class Book(
    val authors: List<String>,
    val categories: List<String>,
    val imageLinks: İmageLinks,
    val title: String
) {
}
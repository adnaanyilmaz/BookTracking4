package com.example.booktracking4.data.remote.dto

import com.example.booktracking4.domain.model.Book


data class Item(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo?,
    val selfLink: String,
    val volumeInfo: VolumeInfo
){

}
// İtem.kt
fun Item.toBook(): Book {
    return Book(
        authors = volumeInfo.authors ?: emptyList(),
        categories = volumeInfo.categories ?: emptyList(),
        imageLinks = volumeInfo.imageLinks ?: ImageLinks("",""), // Burada bir hata olabilir. Değeri null veya uygun bir varsayılan değer olarak ayarlayın.
        title = volumeInfo.title ?: "Unknown Title"
    )
}



package com.example.booktracking4.data.remote.dto.recommended_dto

import com.example.booktracking4.domain.model.retrofit.CategoriesRecommended
import com.onuryasarkaraduman.dto.HomeCategoriesResponse

fun HomeCategoriesResponse.toMapper(): List<CategoriesRecommended> {
    return this.items.map {item ->
        CategoriesRecommended(
            id = item?.id.orEmpty(),
            authors = item?.volumeInfo?.authors.orEmpty(),
            categories = item?.volumeInfo?.categories.orEmpty(),
            imageLink = item?.volumeInfo?.imageLinks?.thumbnail.orEmpty(),
            title = item?.volumeInfo?.title.orEmpty()
        )
    }
}
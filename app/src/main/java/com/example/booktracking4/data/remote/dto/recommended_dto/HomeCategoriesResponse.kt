package com.onuryasarkaraduman.dto


import com.google.gson.annotations.SerializedName

data class HomeCategoriesResponse(
    @SerializedName("items")
    val items: List<Item?> = emptyList(),
    @SerializedName("kind")
    val kind: String,
    @SerializedName("totalItems")
    val totalItems: Int
)
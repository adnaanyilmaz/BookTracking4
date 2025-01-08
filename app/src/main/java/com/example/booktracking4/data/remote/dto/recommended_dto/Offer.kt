package com.onuryasarkaraduman.dto


import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("finskyOfferType")
    val finskyOfferType: Int,
    @SerializedName("listPrice")
    val listPrice: ListPriceX? = null,
    @SerializedName("retailPrice")
    val retailPrice: RetailPrice
)
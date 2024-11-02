package com.example.booktracking4.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()

}
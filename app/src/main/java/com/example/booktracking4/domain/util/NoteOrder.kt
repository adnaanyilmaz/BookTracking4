package com.example.booktracking4.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class IsFavorite(orderType: OrderType): NoteOrder(orderType)
}
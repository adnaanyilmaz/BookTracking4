package com.example.booktracking4.presentation.fragments.bookdetail

sealed class Selection {
    data object WillIRead : Selection()
    data object ReadingNow: Selection()
    data object WhatIRead : Selection()
}

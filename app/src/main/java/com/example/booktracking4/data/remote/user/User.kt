package com.example.booktracking4.data.remote.user

data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val whatIRead: List<WhatIRead> = emptyList(),
    val whatIWillRead: List<WhatIWillRead> = emptyList(),
    val readNow: List<ReadNow> = emptyList()
)

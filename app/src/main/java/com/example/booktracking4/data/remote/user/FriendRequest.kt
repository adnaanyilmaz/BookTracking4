package com.example.booktracking4.data.remote.user

data class FriendRequest(
    val requestId: String = "", // Unique identifier for the request
    val fromUserId: String = "", // User ID of the sender
    val toUserId: String = "", // User ID of the recipient
    val userName: String = "", // Display name of the sender
    val nameSurname: String = "", // Full name of the sender
    val profileImage: String = "" // URL of the sender's profile image (optional)
)
package com.example.booktracking4.common

import android.widget.ImageView
import coil.load
import com.example.booktracking4.R

fun ImageView.loadImageView(imageUrl: String) {
    this.load(imageUrl) {
        crossfade(600)
        error(R.drawable.ic_placeholder)
    }
}
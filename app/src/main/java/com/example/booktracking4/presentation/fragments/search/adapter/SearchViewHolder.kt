package com.example.booktracking4.presentation.fragments.search.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemSearchBookBinding
import com.example.booktracking4.R
import com.squareup.picasso.Picasso


class SearchViewHolder(binding: ItemSearchBookBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(book: com.example.booktracking4.domain.model.Book){
        itemView.findViewById<TextView>(R.id.tvBookTitle).text= book.title
        itemView.findViewById<TextView>(R.id.tvBookAuthors).text=book.authors.getOrNull(0) ?: "Unknown Author"
        itemView.findViewById<TextView>(R.id.tvBookCategory).text=book.categories.getOrNull(0) ?: "Unknown Category"

        Picasso.get()
            .load(book.imageLinks.thumbnail)
            .error(android.R.color.darker_gray)
            .into(itemView.findViewById<ImageView>(R.id.ivBookThumbnail))
    }

}
package com.example.booktracking4.presentation.fragments.favorite_books.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.R
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.databinding.ItemFavoriteBookBinding


class FavoriteBooksAdapter(
    private val onFavoriteClick: (String, Boolean) -> Unit,
) : ListAdapter<Read, FavoriteBooksAdapter.ReadViewHolder>(WhatIReadDiffCallback()) {

    class ReadViewHolder(private val binding: ItemFavoriteBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            read: Read,
            onFavoriteClick: (String, Boolean) -> Unit,
        ) {
            binding.apply {
                ivFavorite.setImageResource(
                    if (read.isFavorite) R.drawable.ic_favorite else R.drawable.favorite_border
                )
                tvBookTitle.text = read.bookName
                tvAuthorName.text=read.authorName
                tvProgress.text=read.pageCount.toString()
                ivUserProfilePhoto.loadImageView(read.image)
                tvBookStatus.text = "Favorite"
                ivFavorite.setOnClickListener{
                    onFavoriteClick.invoke(read.bookId,!read.isFavorite)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val binding =
            ItemFavoriteBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        holder.bind(getItem(position),onFavoriteClick)
    }

    // Submit data method to update the list
    fun submitData(newList: List<Read>) {
        submitList(newList)
    }
}

// DiffUtil callback for efficient updates
class WhatIReadDiffCallback : DiffUtil.ItemCallback<Read>() {
    override fun areItemsTheSame(oldItem: Read, newItem: Read): Boolean {
        return oldItem.bookId == newItem.bookId // Unique identifier for the item
    }

    override fun areContentsTheSame(oldItem: Read, newItem: Read): Boolean {
        return oldItem == newItem // Check if the contents are the same
    }
}

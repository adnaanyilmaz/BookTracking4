package com.example.booktracking4.presentation.fragments.read.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.R
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.databinding.ItemBookReadBinding

class ReadAdapter(
    private val onItemClickListener: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onNavigate: (String) -> Unit,
    private val onFavoriteClick: (String, Boolean) -> Unit,
) : ListAdapter<Read, ReadAdapter.ReadViewHolder>(WhatIReadDiffCallback()) {

    class ReadViewHolder(private val binding: ItemBookReadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            read: Read,
            onItemClickListener: (String) -> Unit,
            onDeleteClick: (String) -> Unit,
            onNavigate: (String) -> Unit,
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
                tvBookStatus.text = "Read"
                cardView.setOnClickListener {
                    onItemClickListener.invoke(read.bookId)
                }
                ivDelete.setOnClickListener {
                    onDeleteClick.invoke(read.bookId)
                    Toast.makeText(cardView.context, "${read.bookName} DELETED.", Toast.LENGTH_SHORT).show()
                }
                btnAddNote.setOnClickListener{
                    onNavigate.invoke(read.bookName)
                }
                ivFavorite.setOnClickListener{
                  onFavoriteClick.invoke(read.bookId,!read.isFavorite)
            }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val binding =
            ItemBookReadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener, onDeleteClick,onNavigate,onFavoriteClick)
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

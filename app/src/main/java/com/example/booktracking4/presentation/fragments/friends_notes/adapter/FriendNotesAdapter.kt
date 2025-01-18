package com.example.booktracking4.presentation.fragments.friends_notes.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemFriendNoteBinding
import com.example.booktracking4.domain.model.room.BookNote
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class FriendNotesAdapter(
    private val onFavoriteClick: (String, Boolean) -> Unit,
    private val userName: String
) : ListAdapter<BookNote, FriendNotesAdapter.ReadViewHolder>(WhatIReadDiffCallback()) {

    class ReadViewHolder(private val binding: ItemFriendNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NewApi")
        fun bind(
            note: BookNote,
            onFavoriteClick: (String, Boolean) -> Unit,
            userName: String
        ) {
            binding.apply {
                tvUserName.text=userName
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content
                tvNotePageRange.text = note.page
                tvNoteDate.text = convertMillisToDate(note.timestamp)
                tvBookName.text = note.bookName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val binding =
            ItemFriendNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        holder.bind(getItem(position),onFavoriteClick,userName)
    }

    // Submit data method to update the list
    fun submitData(newList: List<BookNote>) {
        submitList(newList)
    }
}

// DiffUtil callback for efficient updates
class WhatIReadDiffCallback : DiffUtil.ItemCallback<BookNote>() {
    override fun areItemsTheSame(oldItem: BookNote, newItem: BookNote): Boolean {
        return oldItem.id == newItem.id // Unique identifier for the item
    }

    override fun areContentsTheSame(oldItem: BookNote, newItem: BookNote): Boolean {
        return oldItem == newItem // Check if the contents are the same
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun convertMillisToDate(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

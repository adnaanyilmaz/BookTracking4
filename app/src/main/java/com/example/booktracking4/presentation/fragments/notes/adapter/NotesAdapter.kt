package com.example.booktracking4.presentation.fragments.notes.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter.NoteViewHolder
import com.example.booktracking4.R
import com.example.booktracking4.domain.model.room.BookNote
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class NotesAdapter(
    private val onItemClickListener: (BookNote) -> Unit,
    private val onDeleteClick: ((BookNote) -> Unit)? = null,
    private val onFavoriteClick: (BookNote) -> Unit
) : ListAdapter<BookNote, NotesAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(
            note: BookNote,
            onItemClickListener: (BookNote) -> Unit,
            onDeleteClick: ((BookNote) -> Unit)? = null,
            onFavoriteClick: (BookNote) -> Unit
        ) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content
                tvNotePageRange.text = note.page
                tvNoteDate.text = convertMillisToDate(note.timestamp)
                tvBookName.text = note.bookName
                ivFavorite.setImageResource(
                    if (note.isFavorite) R.drawable.ic_favorite else R.drawable.favorite_border
                )
            }
            binding.cardViewNote.setOnClickListener {
                onItemClickListener.invoke(note)
            }
            binding.ivDelete.setOnClickListener {
                onDeleteClick?.invoke(note)
            }
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick.invoke(note)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertMillisToDate(millis: Long): String {
            val instant = Instant.ofEpochMilli(millis)
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm")
                .withZone(ZoneId.systemDefault())
            return formatter.format(instant)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            onItemClickListener,
            onDeleteClick,
            onFavoriteClick
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookNote>() {
            override fun areItemsTheSame(oldItem: BookNote, newItem: BookNote): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BookNote, newItem: BookNote): Boolean {
                return oldItem == newItem
            }
        }
    }
}

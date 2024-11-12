package com.example.booktracking4.presentation.fragments.notes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter.NoteViewHolder
import com.example.booktracking4.R
import com.example.booktracking4.domain.model.room.BookNote


class NotesAdapter(
    private val itemList: List<BookNote> ,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<NoteViewHolder>() {

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(note: BookNote, onItemClickListener: (Int) -> Unit) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content
                tvNotePageRange.text = note.page
                tvNoteDate.text = note.timestamp.toString()
                ivFavorite.setImageResource(
                    if (note.isFavorite) R.drawable.favorite else R.drawable.favorite_border
                )
            }
            binding.cardViewNote.setOnClickListener{
                onItemClickListener.invoke(note.id?.toInt() ?: 1 )
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int
    ) {
        holder.bind(itemList[position], onItemClickListener)
    }
}
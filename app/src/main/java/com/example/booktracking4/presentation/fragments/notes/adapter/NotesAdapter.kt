package com.example.booktracking4.presentation.fragments.notes.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter.NoteViewHolder
import com.example.booktracking4.R
import com.example.booktracking4.domain.model.room.BookNote
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class NotesAdapter(
    private val itemList: List<BookNote> ,
    private val onItemClickListener: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit

) : RecyclerView.Adapter<NoteViewHolder>() {

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(note: BookNote, onItemClickListener: (Int) -> Unit,onDeleteClick: (Int) -> Unit) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content
                tvNotePageRange.text = note.page
                tvNoteDate.text = convertMillisToDate(note.timestamp)
                ivFavorite.setImageResource(
                    if (note.isFavorite) R.drawable.favorite else R.drawable.favorite_border
                )
            }
            binding.cardViewNote.setOnClickListener{
                onItemClickListener.invoke(note.id?.toInt() ?: 1 )
            }
            binding.cardViewNote.setOnClickListener{
                onDeleteClick.invoke(note.id?.toInt() ?: 1 )
            }


        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertMillisToDate(millis: Long): String{
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

    override fun getItemCount(): Int = itemList.size


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int
    ) {
        holder.bind(itemList[position], onItemClickListener,onDeleteClick)
    }


}
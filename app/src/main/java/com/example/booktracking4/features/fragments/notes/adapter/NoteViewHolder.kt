package com.example.booktracking4.features.fragments.notes.adapter


import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.R
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel

class NoteViewHolder(binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(note: NotesModel) {
        itemView.findViewById<TextView>(R.id.tvNoteTitle).text = note.title
        itemView.findViewById<TextView>(R.id.tvNoteContent).text = note.content
        itemView.findViewById<TextView>(R.id.tvNotePageRange).text = "Page: ${note.pageRange}"
        itemView.findViewById<TextView>(R.id.tvNoteDate).text = note.date


        // Favori simgesini güncelle
        itemView.findViewById<ImageView>(R.id.ivFavorite).setImageResource(
            if (note.isFavorite) R.drawable.favorite else R.drawable.favorite_border
        )

        // Not tıklandığında ne yapılacağını belirt
//        binding.root.setOnClickListener {
//            onNoteClick(note)
//        }
    }

}
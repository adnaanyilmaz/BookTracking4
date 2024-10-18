package com.example.booktracking4.features.fragments.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel


class NotesAdapter(private val itemList: List<NotesModel>):RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding=ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}
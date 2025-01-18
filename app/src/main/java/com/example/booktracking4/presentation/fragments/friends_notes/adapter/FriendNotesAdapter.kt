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


class FriendNotesAdapter :
    ListAdapter<Pair<String, BookNote>, FriendNotesAdapter.NoteViewHolder>(FriendNotesDiffCallback()) {

    class NoteViewHolder(private val binding: ItemFriendNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "NewApi")
        fun bind(username: String, note: BookNote) {
            binding.apply {
                tvUserName.text = username
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content
                tvNotePageRange.text = note.page
                tvNoteDate.text = convertMillisToDate(note.timestamp)
                tvBookName.text = note.bookName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            ItemFriendNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val (username, note) = getItem(position)
        holder.bind(username, note)
    }

    // Submit data method to update the list
    fun submitData(newList: List<Pair<String, BookNote>>) {
        submitList(newList)
    }
}

// DiffUtil callback for efficient updates
class FriendNotesDiffCallback : DiffUtil.ItemCallback<Pair<String, BookNote>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, BookNote>,
        newItem: Pair<String, BookNote>
    ): Boolean {
        return oldItem.second.id == newItem.second.id // Unique identifier for the note
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, BookNote>,
        newItem: Pair<String, BookNote>
    ): Boolean {
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

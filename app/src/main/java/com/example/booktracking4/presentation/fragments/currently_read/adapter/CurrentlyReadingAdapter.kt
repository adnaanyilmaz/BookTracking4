package com.example.booktracking4.presentation.fragments.currently_read.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter.CurrentlyReadingViewHolder

class CurrentlyReadingAdapter(
    private val onItemClickListener: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onNavigate: (String) -> Unit,
) : ListAdapter<CurrentlyReading, CurrentlyReadingViewHolder>(ReadNowDiffCallback()) {


    class CurrentlyReadingViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentlyReading: CurrentlyReading,
            onItemClickListener: (String) -> Unit,
            onDeleteClick: (String) -> Unit,
            onNavigate: (String) -> Unit
        ) {
            binding.apply {
                tvBookTitle.text = currentlyReading.bookName
                ivUserProfilePhoto.loadImageView(currentlyReading.image)
                cardView.setOnClickListener {
                    onItemClickListener.invoke(currentlyReading.bookId)
                }
                ivDelete.setOnClickListener {
                    onDeleteClick.invoke(currentlyReading.bookId)
                    Toast.makeText(cardView.context, "${currentlyReading.bookName} DELETED.", Toast.LENGTH_SHORT).show()
                }
                btnAddNote.setOnClickListener{
                    onNavigate.invoke(currentlyReading.bookId)
                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentlyReadingViewHolder {
        val binding =
            ItemBookStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrentlyReadingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentlyReadingViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener, onDeleteClick,onNavigate)
    }

    fun submitData(newList: List<CurrentlyReading>) {
        submitList(newList)
    }

}

// DiffUtil callback for efficient updates
class ReadNowDiffCallback : DiffUtil.ItemCallback<CurrentlyReading>() {
    override fun areItemsTheSame(oldItem: CurrentlyReading, newItem: CurrentlyReading): Boolean {
        return oldItem.bookId == newItem.bookId // Unique identifier for the item
    }

    override fun areContentsTheSame(oldItem: CurrentlyReading, newItem: CurrentlyReading): Boolean {
        return oldItem == newItem // Check if the contents are the same
    }
}
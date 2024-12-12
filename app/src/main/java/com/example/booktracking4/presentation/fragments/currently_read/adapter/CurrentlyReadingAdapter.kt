package com.example.booktracking4.presentation.fragments.currently_read.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.ReadNow
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter.CurrentlyReadingViewHolder

class CurrentlyReadingAdapter(
    private val onItemClickListener: (String) -> Unit
) : ListAdapter<ReadNow, CurrentlyReadingViewHolder>(ReadNowDiffCallback()) {


    class CurrentlyReadingViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(readNow: ReadNow, onItemClickListener: (String) -> Unit) {
            binding.apply {
                tvBookTitle.text = readNow.bookName
                ivUserProfilePhoto.loadImageView(readNow.image)
                linearLaoyut.setOnClickListener {
                    onItemClickListener.invoke(readNow.bookId)
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
        holder.bind(getItem(position), onItemClickListener)
    }

    fun submitData(newList: List<ReadNow>) {
        submitList(newList)
    }

}

// DiffUtil callback for efficient updates
class ReadNowDiffCallback : DiffUtil.ItemCallback<ReadNow>() {
    override fun areItemsTheSame(oldItem: ReadNow, newItem: ReadNow): Boolean {
        return oldItem.bookId == newItem.bookId // Unique identifier for the item
    }

    override fun areContentsTheSame(oldItem: ReadNow, newItem: ReadNow): Boolean {
        return oldItem == newItem // Check if the contents are the same
    }
}
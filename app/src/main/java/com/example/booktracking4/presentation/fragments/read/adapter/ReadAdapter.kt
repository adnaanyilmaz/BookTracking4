package com.example.booktracking4.presentation.fragments.read.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.WhatIRead
import com.example.booktracking4.databinding.ItemBookStatusBinding

class ReadAdapter(
    private val onItemClickListener: (String) -> Unit
) : ListAdapter<WhatIRead, ReadAdapter.ReadViewHolder>(WhatIReadDiffCallback()) {

    class ReadViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(whatIRead: WhatIRead, onItemClickListener: (String) -> Unit) {
            binding.apply {
                tvBookTitle.text = whatIRead.bookName
                ivUserProfilePhoto.loadImageView(whatIRead.image)
                linearLaoyut.setOnClickListener {
                    onItemClickListener.invoke(whatIRead.bookId)
                }
                tvBookStatus.text = "Read"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val binding = ItemBookStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    // Submit data method to update the list
    fun submitData(newList: List<WhatIRead>) {
        submitList(newList)
    }
}

// DiffUtil callback for efficient updates
class WhatIReadDiffCallback : DiffUtil.ItemCallback<WhatIRead>() {
    override fun areItemsTheSame(oldItem: WhatIRead, newItem: WhatIRead): Boolean {
        return oldItem.bookId == newItem.bookId // Unique identifier for the item
    }

    override fun areContentsTheSame(oldItem: WhatIRead, newItem: WhatIRead): Boolean {
        return oldItem == newItem // Check if the contents are the same
    }
}

package com.example.booktracking4.presentation.fragments.to_read.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.WhatIWillRead
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.to_read.adapter.ToReadAdapter.ToReadViewHolder

class ToReadAdapter(
    private val onItemClickListener: (String) -> Unit
) : ListAdapter<WhatIWillRead, ToReadViewHolder>(WillIReadDiffCallback()) {

    class ToReadViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(willRead: WhatIWillRead, onItemClickListener: (String) -> Unit) {
            binding.apply {
                tvBookTitle.text = willRead.bookName
                ivUserProfilePhoto.loadImageView(willRead.image)
                linearLaoyut.setOnClickListener {
                    onItemClickListener.invoke(willRead.bookId)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToReadViewHolder {
        val binding =
            ItemBookStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToReadViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ToReadViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onItemClickListener)
    }

    fun submitData(newList: List<WhatIWillRead>) {
        submitList(newList)
    }

    // DiffUtil callback for efficient updates
    class WillIReadDiffCallback : DiffUtil.ItemCallback<WhatIWillRead>() {
        override fun areItemsTheSame(oldItem: WhatIWillRead, newItem: WhatIWillRead): Boolean {
            return oldItem.bookId == newItem.bookId // Unique identifier for the item
        }

        override fun areContentsTheSame(oldItem: WhatIWillRead, newItem: WhatIWillRead): Boolean {
            return oldItem == newItem // Check if the contents are the same
        }
    }
}
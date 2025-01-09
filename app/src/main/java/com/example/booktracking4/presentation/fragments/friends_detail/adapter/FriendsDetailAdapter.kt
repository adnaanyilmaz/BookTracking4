package com.example.booktracking4.presentation.fragments.friends_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.databinding.ItemBookCardBinding

class FriendsDetailAdapter(
    private val onItemClickListener: (String) -> Unit,

    ) : ListAdapter<Read, FriendsDetailAdapter.FriendRequestViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemBookCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FriendRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendRequestViewHolder(
        private val binding: ItemBookCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(readBook: Read) {
            binding.apply {
                tvBookTitle.text = readBook.bookName
                tvBookAuthors.text = readBook.authorName.toString()
                tvBookCategory.text=readBook.category
                ivBookThumbnail.loadImageView(readBook.image)
                linearLaoyut.setOnClickListener{
                    onItemClickListener.invoke(readBook.bookId)
                }

            }


        }
    }
    // Submit data method to update the list
    fun submitData(newList: List<Read>) {
        submitList(newList)
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Read>() {
        override fun areItemsTheSame(oldItem: Read, newItem: Read): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: Read, newItem: Read): Boolean {
            return oldItem == newItem // İçerik aynı mı
        }
    }
}




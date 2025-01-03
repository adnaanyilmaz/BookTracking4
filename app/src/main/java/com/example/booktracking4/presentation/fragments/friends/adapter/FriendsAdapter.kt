package com.example.booktracking4.presentation.fragments.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.Friends
import com.example.booktracking4.databinding.ItemFriendsBinding

class FriendsAdapter(
    private val onItemClickListener: (String) -> Unit

    ) : ListAdapter<Friends, FriendsAdapter.FriendRequestViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemFriendsBinding.inflate(
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
        private val binding: ItemFriendsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendsList: Friends) {
            binding.tvUserName.text = friendsList.userName
            binding.tvEmail.text = friendsList.email
            binding.cardViewSearch.setOnClickListener{
                onItemClickListener.invoke(friendsList.uid)
            }
        }
    }
    // Submit data method to update the list
    fun submitData(newList: List<Friends>) {
        submitList(newList)
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Friends>() {
        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem // İçerik aynı mı
        }
    }
}

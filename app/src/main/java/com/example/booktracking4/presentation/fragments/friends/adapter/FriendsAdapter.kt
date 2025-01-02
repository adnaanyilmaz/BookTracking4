package com.example.booktracking4.presentation.fragments.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.Friends
import com.example.booktracking4.databinding.ItemFriendsBinding

class FriendsAdapter(
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
        fun bind(friendRequest: Friends) {
            binding.tvUserName.text = friendRequest.userName
            binding.tvEmail.text = friendRequest.email

        }
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

package com.example.booktracking4.presentation.fragments.friends_requests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.FriendRequest
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.databinding.ItemFriendsBinding
import com.example.booktracking4.databinding.ItemRequestFriendBinding



class FriendRequestsAdapter(
    private val onAcceptClicked: (String) -> Unit,
    private val onRejectClicked: (String) -> Unit
) : ListAdapter<FriendRequest, FriendRequestsAdapter.FriendRequestViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemRequestFriendBinding.inflate(
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
        private val binding: ItemRequestFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendRequest: FriendRequest) {
            binding.tvUserName.text = friendRequest.userName
            binding.tvNameSurname.text = friendRequest.nameSurname
            // Load image using Glide or similar library
            // Glide.with(binding.ivUserProfileThumbnail).load(friendRequest.profileImage).into(binding.ivUserProfileThumbnail)

            binding.btnAccept.setOnClickListener {
                onAcceptClicked(friendRequest.requestId)
            }
            binding.btnReject.setOnClickListener {
                onRejectClicked(friendRequest.requestId)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
        override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem.requestId == newItem.requestId
        }

        override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
            return oldItem == newItem
        }
    }
}

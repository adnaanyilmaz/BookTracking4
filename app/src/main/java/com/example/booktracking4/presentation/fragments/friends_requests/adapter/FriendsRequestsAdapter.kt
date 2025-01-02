package com.example.booktracking4.presentation.fragments.friends_requests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.FriendsRequest
import com.example.booktracking4.databinding.ItemRequestFriendBinding


class FriendRequestsAdapter(
    private val onAcceptClicked: (String) -> Unit,
    private val onRejectClicked: (String) -> Unit
) : ListAdapter<FriendsRequest, FriendRequestsAdapter.FriendRequestViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding = ItemRequestFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendRequestViewHolder(
        private val binding: ItemRequestFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friendRequest: FriendsRequest) {
            binding.tvUserName.text = friendRequest.userName
            binding.tvEmail.text = friendRequest.email



            binding.btnAccept.setOnClickListener {
                onAcceptClicked(friendRequest.userName)
            }
            binding.btnReject.setOnClickListener {
                onRejectClicked(friendRequest.userName)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FriendsRequest>() {
        override fun areItemsTheSame(oldItem: FriendsRequest, newItem: FriendsRequest): Boolean {
            return oldItem.uid==newItem.uid
        }

        override fun areContentsTheSame(oldItem: FriendsRequest, newItem: FriendsRequest): Boolean {
            return oldItem == newItem // İçerik aynı mı
        }
    }
}

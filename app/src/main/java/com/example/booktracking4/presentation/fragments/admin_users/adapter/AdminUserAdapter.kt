package com.example.booktracking4.presentation.fragments.admin_users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.databinding.ItemUsersBinding


class AdminUserAdapter(
    private val onItemClickListener: (String) -> Unit

) : ListAdapter<User, AdminUserAdapter.AdminUserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserViewHolder {
        val binding = ItemUsersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AdminUserViewHolder(
        private val binding: ItemUsersBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUserName.text = user.userName
            binding.tvEmail.text = user.email
            binding.ivDelete.setOnClickListener{
                onItemClickListener.invoke(user.uid)
            }
        }
    }
    // Submit data method to update the list
    fun submitData(newList: List<User>) {
        submitList(newList)
    }
    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem // İçerik aynı mı
        }
    }
}

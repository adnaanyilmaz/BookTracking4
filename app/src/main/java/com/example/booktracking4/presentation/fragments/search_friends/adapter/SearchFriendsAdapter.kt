package com.example.booktracking4.presentation.fragments.search_friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.databinding.ItemAddFriendsBinding

// Kullanıcı modeline göre adapter
class SearchFriendsAdapter(
    private val onAddFriendClicked: (String) -> Unit // Arkadaş ekleme butonu için tıklama işlevi
) : ListAdapter<User, SearchFriendsAdapter.SearchFriendsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendsViewHolder {
        val binding = ItemAddFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchFriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchFriendsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchFriendsViewHolder(
        private val binding: ItemAddFriendsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // ViewHolder'daki verileri bağlama
        fun bind(user: User) {

            binding.tvUserName.text = user.userName
            binding.tvNameSurname.text = user.email

            // Arkadaş ekleme butonuna tıklama
            binding.btnAddFriend.setOnClickListener {
                onAddFriendClicked(user.userName)
            }

        }
    }

    // Farklılık kontrolü için DiffUtil
    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid // Benzersiz kullanıcı ID'sine göre kontrol
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem // Kullanıcıların tüm içeriğini karşılaştırır
        }
    }
}

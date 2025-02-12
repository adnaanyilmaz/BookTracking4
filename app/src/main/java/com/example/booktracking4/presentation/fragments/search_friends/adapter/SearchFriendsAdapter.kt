package com.example.booktracking4.presentation.fragments.search_friends.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.databinding.ItemAddFriendsBinding
import com.example.booktracking4.R
import com.example.booktracking4.presentation.fragments.search_friends.SearchFriendsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Kullanıcı modeline göre adapter
class SearchFriendsAdapter(
    private val onAddFriendClicked: (String) -> Unit, // Arkadaş ekleme butonu için tıklama işlevi

    val viewModel: SearchFriendsViewModel,
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
        @SuppressLint("ResourceAsColor")
        fun bind(user: User) {
            binding.tvUserName.text = user.userName
            binding.tvNameSurname.text = user.email


            CoroutineScope(Dispatchers.Main).launch {
                val buttonClickState = viewModel.checkFriendsState(uid = user.uid)
                if (buttonClickState) {
                    binding.btnAddFriend.isEnabled = false
                    binding.btnAddFriend.backgroundTintList = ContextCompat.getColorStateList(
                        binding.btnAddFriend.context,
                        R.color.gray
                    )
                }
            }


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

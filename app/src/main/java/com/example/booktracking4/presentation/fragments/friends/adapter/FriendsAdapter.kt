package com.example.booktracking4.presentation.fragments.friends.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemFriendsBinding
import com.example.booktracking4.presentation.fragments.friends.adapter.FriendsAdapter.FriendsViewHolder
import com.example.booktracking4.presentation.fragments.search.adapter.SearchAdapter.SearchBooksViewHolder

class FriendsAdapter() : RecyclerView.Adapter<FriendsViewHolder>() {

    class FriendsViewHolder(private val binding: ItemFriendsBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(){
                binding.apply {

                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FriendsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
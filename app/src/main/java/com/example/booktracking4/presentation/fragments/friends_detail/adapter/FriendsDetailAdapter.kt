package com.example.booktracking4.presentation.fragments.friends_detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemBookCardBinding
import com.example.booktracking4.domain.model.retrofit.Book
import com.example.booktracking4.presentation.fragments.friends_detail.adapter.FriendsDetailAdapter.FriendsDetailViewHolder

class FriendsDetailAdapter(
    private val itemList: List<Book>, private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<FriendsDetailViewHolder>() {


    class FriendsDetailViewHolder(private val binding: ItemBookCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FriendsDetailViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FriendsDetailViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}



package com.example.booktracking4.presentation.fragments.to_read.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.to_read.adapter.ToReadAdapter.ToReadViewHolder

class ToReadAdapter() : RecyclerView.Adapter<ToReadViewHolder>() {

    class ToReadViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToReadViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ToReadViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
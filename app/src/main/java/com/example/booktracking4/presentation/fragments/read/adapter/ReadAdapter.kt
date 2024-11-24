package com.example.booktracking4.presentation.fragments.read.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.read.adapter.ReadAdapter.ReadViewHolder

class ReadAdapter() : RecyclerView.Adapter<ReadViewHolder>() {

    class ReadViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReadViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
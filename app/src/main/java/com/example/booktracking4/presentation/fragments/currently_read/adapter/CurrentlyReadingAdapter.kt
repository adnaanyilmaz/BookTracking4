package com.example.booktracking4.presentation.fragments.currently_read.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.FragmentCurrentlyReadingBinding
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter.CurrentlyReadingViewHolder

class CurrentlyReadingAdapter() : RecyclerView.Adapter<CurrentlyReadingViewHolder>() {


    class CurrentlyReadingViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(){

            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentlyReadingViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CurrentlyReadingViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
package com.example.booktracking4.presentation.fragments.home.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.R
import com.example.booktracking4.databinding.ItemLayoutHomepageListBinding
import com.example.booktracking4.domain.model.ui_model.home_model.HomeModel

class HomePageViewHolder(binding: ItemLayoutHomepageListBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(homeModel: HomeModel){
        itemView.findViewById<ImageView>(R.id.ivBooksImage).setImageResource(homeModel.bookImage)
        itemView.findViewById<TextView>(R.id.tvBookName).text=homeModel.bookName
        itemView.findViewById<TextView>(R.id.tvAuthorName).text=homeModel.bookAuthor

    }
}
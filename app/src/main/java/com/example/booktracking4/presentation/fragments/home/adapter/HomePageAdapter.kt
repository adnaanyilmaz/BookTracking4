package com.example.booktracking4.presentation.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.databinding.ItemLayoutHomepageListBinding
import com.example.booktracking4.domain.model.ui_model.home_model.HomeModel

class HomePageAdapter(private val itemList: List<HomeModel>) :
    RecyclerView.Adapter<HomePageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val binding =ItemLayoutHomepageListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePageViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}
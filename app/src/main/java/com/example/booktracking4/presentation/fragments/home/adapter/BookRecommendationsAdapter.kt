package com.example.booktracking4.presentation.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.databinding.ItemBookRecommendationBinding
import com.example.booktracking4.domain.model.retrofit.CategoriesRecommended

class BookRecommendationsAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<CategoriesRecommended, BookRecommendationsAdapter.BookViewHolder>(DiffCallback) {

    inner class BookViewHolder(private val binding: ItemBookRecommendationBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(book: CategoriesRecommended) {
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.authors.toString()
            binding.tvBookCategory.text = book.categories.toString()

            binding.ivBookCover.loadImageView(book.imageLink.toString()) // Extension function to load image
            binding.root.setOnClickListener { onItemClick(book.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookRecommendationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CategoriesRecommended>() {
        override fun areItemsTheSame(
            oldItem: CategoriesRecommended,
            newItem: CategoriesRecommended
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CategoriesRecommended,
            newItem: CategoriesRecommended
        ): Boolean = oldItem == newItem
    }
}

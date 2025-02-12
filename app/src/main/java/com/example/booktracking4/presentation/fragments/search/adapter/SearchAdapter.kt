package com.example.booktracking4.presentation.fragments.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.databinding.ItemBookCardBinding
import com.example.booktracking4.domain.model.retrofit.Book
import com.example.booktracking4.presentation.fragments.search.adapter.SearchAdapter.SearchBooksViewHolder

class SearchAdapter(
    private val itemList: List<Book>,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.Adapter<SearchBooksViewHolder>() {

    class SearchBooksViewHolder(private val binding: ItemBookCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, onItemClickListener: (String) -> Unit) {
            binding.apply {
                tvBookTitle.text = book.title
                tvBookAuthors.text = book.authors.getOrNull(0) ?: "Unknown Author"
                tvBookCategory.text = book.categories.getOrNull(0) ?: "Unknown Category"
                ivBookThumbnail.loadImageView(book.imageLinks.thumbnail)
            }
            binding.cardViewSearch.setOnClickListener {
                onItemClickListener.invoke(book.id)
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SearchBooksViewHolder {
        val binding =
            ItemBookCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchBooksViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchBooksViewHolder, position: Int
    ) {
        holder.bind(itemList[position], onItemClickListener)
    }

    override fun getItemCount(): Int = itemList.size


}
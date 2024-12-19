package com.example.booktracking4.presentation.fragments.to_read.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.WantToRead
import com.example.booktracking4.databinding.ItemBookStatusBinding
import com.example.booktracking4.presentation.fragments.to_read.adapter.ToReadAdapter.ToReadViewHolder

class ToReadAdapter(
    private val onItemClickListener: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onNavigate: (String) -> Unit,
) : ListAdapter<WantToRead, ToReadViewHolder>(WillIReadDiffCallback()) {

    class ToReadViewHolder(private val binding: ItemBookStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            wantToRead: WantToRead,
            onItemClickListener: (String) -> Unit,
            onDeleteClick: (String) -> Unit,
            onNavigate: (String) -> Unit
        ) {
            binding.apply {
                tvBookTitle.text = wantToRead.bookName
                ivUserProfilePhoto.loadImageView(wantToRead.image)
                cardView.setOnClickListener {
                    onItemClickListener.invoke(wantToRead.bookId)
                }
                ivDelete.setOnClickListener {
                    onDeleteClick.invoke(wantToRead.bookId)
                    Toast.makeText(cardView.context,"${wantToRead.bookName} DELETED.", Toast.LENGTH_SHORT).show()
                }
                btnAddNote.setOnClickListener{
                    onNavigate.invoke(wantToRead.bookId)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToReadViewHolder {
        val binding =
            ItemBookStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToReadViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ToReadViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onItemClickListener, onDeleteClick,onNavigate)
    }

    fun submitData(newList: List<WantToRead>) {
        submitList(newList)
    }

    // DiffUtil callback for efficient updates
    class WillIReadDiffCallback : DiffUtil.ItemCallback<WantToRead>() {
        override fun areItemsTheSame(oldItem: WantToRead, newItem: WantToRead): Boolean {
            return oldItem.bookId == newItem.bookId // Unique identifier for the item
        }

        override fun areContentsTheSame(oldItem: WantToRead, newItem: WantToRead): Boolean {
            return oldItem == newItem // Check if the contents are the same
        }
    }
}
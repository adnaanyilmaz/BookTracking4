package com.example.booktracking4.presentation.fragments.bookdetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.R
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.databinding.FragmentBookDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: BookDetailFragmentArgs by navArgs()
        val id: String = bundle.id

        viewModel.id = id


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    // Yükleniyor durumunu kontrol et
                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    // Hata mesajını göster
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    }

                    // Kitap listesini al
                    val book = state.book



                    binding.apply {
                        tvBookTitle.text = book?.title
                        tvAuthor.text = book?.authors?.getOrNull(0) ?: "Unknown Author"
                        tvCategories.text = book?.categories?.getOrNull(0) ?: "Unknown Author"
                        book?.imageLinks?.let { ivBookCover.loadImageView(it.thumbnail) }
                        book?.ratingsCount?.let { ratingBar.rating = it.toFloat() }
                        tvDescription.text = book?.description ?: ""
                        tvISBN.text = book?.industryIdentifiers.toString()
                        tvPageCount.text = book?.pageCount.toString()
                        tvPublicationDate.text = book?.publishedDate
                        tvPublisher.text = book?.publisher
                        spinnerOptions
                        btnAddToList.setOnClickListener {

                        }
                    }
                }
            }
        }
    }


}

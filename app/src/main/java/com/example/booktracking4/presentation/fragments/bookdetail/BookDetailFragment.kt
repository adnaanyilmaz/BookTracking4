package com.example.booktracking4.presentation.fragments.bookdetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.data.remote.user.CurrentlyReading
import com.example.booktracking4.data.remote.user.Read
import com.example.booktracking4.data.remote.user.WantToRead
import com.example.booktracking4.databinding.FragmentBookDetailBinding
import com.example.booktracking4.domain.model.retrofit.BookDetail
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

    @SuppressLint("UnsafeRepeatOnLifecycleDetector", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: BookDetailFragmentArgs by navArgs()
        val id: String = bundle.id

        viewModel.id = id

        val options = listOf("Read", "CurrentlyReading", "Want to Read")
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        var selectedOption: Selection = Selection.Read

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOption = when (options[position]) {
                    "Read" -> Selection.Read
                    "CurrentlyReading" -> Selection.CurrentlyReading
                    "Want to Read" -> Selection.WantToRead
                    else -> Selection.Read
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Bir şey seçilmediğinde yapılacak işlemler
            }
        }

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
                        tvBookTitle.text =  book?.title
                        tvAuthor.text = "Author: ${book?.authors?.getOrNull(0)}" ?: "Unknown Author"
                        tvCategories.text = "Categories: ${book?.categories?.getOrNull(0)}" ?: "Unknown Author"
                        book?.imageLinks?.let { ivBookCover.loadImageView(it.thumbnail) }
                        val description: String = book?.description ?: ""
                        val cleanDescription =
                            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
                        tvDescription.text = cleanDescription
                        tvISBN.text = "ISBN: ${book?.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier ?: book?.industryIdentifiers?.get(0)?.identifier}"
                        Log.d("isbn", book?.industryIdentifiers.toString())
                        tvPageCount.text = "Page Count: ${book?.pageCount.toString()}"
                        tvPublicationDate.text = "Publication Date: ${book?.publishedDate.toString()}"
                        tvPublisher.text = "Publisher: ${book?.publisher}"
                        btnAddToList.setOnClickListener {

                            addToList(selection = selectedOption, bookDetail = book)
                            findNavController().popBackStack()
//                            Toast.makeText(
//                                requireContext(),
//                                "$selectedOption added",
//                                Toast.LENGTH_SHORT
//                            ).show()

                        }
                    }
                }
            }
        }
    }

    private fun addToList(selection: Selection, bookDetail: BookDetail?) {
        val userId = viewModel.getUserId()
        when (selection) {
            is Selection.Read -> {
                val read = Read(
                    bookId = bookDetail?.id.orEmpty(),
                    bookName = bookDetail?.title.orEmpty(),
                    image = bookDetail?.imageLinks?.thumbnail.orEmpty(),
                    authorName = bookDetail?.authors?.get(0).orEmpty(),
                    pageCount = bookDetail?.pageCount,
                    category = bookDetail?.categories?.first().orEmpty()
                )
                viewModel.addBookRead(userId = userId, book = read)
            }

            is Selection.CurrentlyReading -> {
                val currentlyReading = CurrentlyReading(
                    bookId = bookDetail?.id.orEmpty(),
                    bookName = bookDetail?.title.orEmpty(),
                    image = bookDetail?.imageLinks?.thumbnail.orEmpty(),
                    authorName = bookDetail?.authors?.get(0).orEmpty(),
                    pageCount = bookDetail?.pageCount
                )
                viewModel.addBookCurrentlyReading(userId = userId, book = currentlyReading)
            }

            is Selection.WantToRead -> {
                val willIRead = WantToRead(
                    bookId = bookDetail?.id.orEmpty(),
                    bookName = bookDetail?.title.orEmpty(),
                    image = bookDetail?.imageLinks?.thumbnail.orEmpty(),
                    authorName = bookDetail?.authors?.get(0).orEmpty(),
                    pageCount = bookDetail?.pageCount
                )
                viewModel.addBookWantToRead(userId = userId, book = willIRead)
            }
        }

    }


}

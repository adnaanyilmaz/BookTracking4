package com.example.booktracking4.presentation.fragments.addnote

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentAddNoteBinding
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.presentation.fragments.addnote.AddNoteViewModel.UiEvent
import com.example.booktracking4.presentation.fragments.bookdetail.BookDetailFragmentArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: AddNoteFragmentArgs by navArgs()
        val id: Int = bundle.id
        val bookName: String=bundle.bookname

        viewModel.updateBookName(bookName)
        viewModel.updateData(id)
        viewModel.loadNoteDetails(id)




        observeViewModel()
        setupUI()
        handleEvents()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        //Tarih
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val formattedDate = dateFormat.format(currentDate)

        binding.apply {
            //date
            tvNoteDate.text = formattedDate
            //title
            etNoteTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredTitle(text.toString()))
            }
            //content
            etNoteContent.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredContent(text.toString()))
            }
            //page number
            etNotePage.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredPageNumber(text.toString()))
            }
            //isFavorite
            ivFavorite.setOnClickListener {
                viewModel.toggleFavorite()
            }
            //save button
            btnSave.setOnClickListener {
                viewModel.onEvent(AddNoteEvent.SaveNote)
            }
            //cancel button
            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.noteTitle.collectLatest { state ->

                        if (binding.etNoteTitle.text.toString() != state.text) {
                            binding.etNoteTitle.setText(state.text)
                        }
                    }
                }
                launch {
                    viewModel.noteContent.collectLatest { state ->
                        if (binding.etNoteContent.text.toString() != state.text) {
                            binding.etNoteContent.setText(state.text)
                        }

                    }
                }
                launch {
                    viewModel.pageCount.collectLatest { state ->
                        if (binding.etNotePage.text.toString() != state.text) {
                            binding.etNotePage.setText(state.text)
                        }

                    }
                }
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.isFavorite.collectLatest { isFavorite ->
                            val imageRes = if (isFavorite) {
                                R.drawable.ic_favorite  // Favori aktif
                            } else {
                                R.drawable.favorite_border  // Favori değil
                            }
                            binding.ivFavorite.setImageResource(imageRes)
                        }
                    }
                }
            }
        }
    }


    private fun handleEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        }

                        UiEvent.SaveNote -> {
                            // Not kaydedildiğinde bir işlem yapın (örn. Geri git)
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

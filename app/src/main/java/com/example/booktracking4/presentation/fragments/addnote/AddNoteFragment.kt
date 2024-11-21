package com.example.booktracking4.presentation.fragments.addnote

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentAddNoteBinding
import com.example.booktracking4.presentation.fragments.addnote.AddNoteViewModel.UiEvent
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

        viewModel.id=id



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
        binding.tvNoteDate.text=formattedDate

        // Başlık alanı dinleyici
        binding.etNoteTitle.apply {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.onEvent(AddNoteEvent.ChangeTitleFocus(hasFocus))
            }
            doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredTitle(text.toString()))
            }
        }

        // İçerik alanı dinleyici
        binding.etNoteContent.apply {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.onEvent(AddNoteEvent.ChangeContentFocus(hasFocus))
            }
            doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredContent(text.toString()))
            }
        }

        // Sayfa numarası dinleyici
        binding.etNotePage.apply {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.onEvent(AddNoteEvent.ChangePageNumberFocus(hasFocus))
            }
            doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddNoteEvent.EnteredPageNumber(text.toString()))
            }
        }

        // Favori durumu
        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }

        // Kaydet butonu
        binding.btnSave.setOnClickListener {
            viewModel.onEvent(AddNoteEvent.SaveNote)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.noteTitle.collectLatest { state ->
                        binding.etNoteTitle.hint = state.hint
                        if (state.isHintVisible) {
                            binding.etNoteTitle.hint = state.hint
                        }
                    }
                }
                launch {
                    viewModel.noteContent.collectLatest { state ->
                        binding.etNoteContent.hint = state.hint
                        if (state.isHintVisible) {
                            binding.etNoteContent.hint = state.hint
                        }
                    }
                }
                launch {
                    viewModel.pageCount.collectLatest { state ->
                        binding.etNotePage.hint = state.hint
                        if (state.isHintVisible) {
                            binding.etNotePage.hint = state.hint
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

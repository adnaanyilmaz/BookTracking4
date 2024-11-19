package com.example.booktracking4.presentation.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentNotesBinding
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.booktracking4.R
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType
import com.google.android.material.snackbar.Snackbar


@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Add Note Navigation
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(0)
            )
        }

        setupRecyclerView()
        setUpRadioGroup()

    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onItemClickListener = { note ->
                findNavController().navigate(
                    NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(note.id ?: 1)
                )
            },
            onDeleteClick = { note ->
                viewModel.onEvent(NotesEvent.DeleteNote(note))
            },
            itemList = viewModel.state.value.notes,
            onFavoriteClick = { note ->
                viewModel.onEvent(NotesEvent.ToggleFavorite(note))
                showUndoSnackBar(note)
            }
        )
        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }
    private fun NotesFragment.showUndoSnackBar(note: BookNote) {
        Snackbar.make(
            binding.root,
            "${note.title} Deleted",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            viewModel.onEvent(NotesEvent.RestoreNote)
        }.show()
    }


    private fun setUpRadioGroup() {
        binding.radioGroupNotes.setOnCheckedChangeListener { _, checkedId ->
            val noteOrder = when (checkedId) {
                R.id.rbAscending -> NoteOrder.Date(OrderType.Ascending)
                R.id.rbDescending -> NoteOrder.Date(OrderType.Descending)
                else -> return@setOnCheckedChangeListener
            }
            viewModel.onEvent(NotesEvent.Order(noteOrder))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val noteOrder = when (item.itemId) {
            R.id.actionFilterTitle -> {
                NoteOrder.Title(OrderType.Descending)
                return true
            }

            R.id.actionFilterDate -> {
                NoteOrder.Date(OrderType.Descending)
                return true
            }

            R.id.actionFilterFavorite -> {
                NoteOrder.IsFavorite(OrderType.Descending)
                return true
            }

            else -> {
                return false
            }
        }
        viewModel.onEvent(noteOrder)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



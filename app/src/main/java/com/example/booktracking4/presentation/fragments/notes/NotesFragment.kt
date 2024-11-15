package com.example.booktracking4.presentation.fragments.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentNotesBinding
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.booktracking4.R
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType


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



//        val notes = listOf(
//            BookNote("Note Title", "This is content", 0L, "10", true, 1),
//            BookNote("Note Title", "This is content", 0L, "10", true, 1),
//            BookNote("Note Title", "This is content", 0L, "10", false, 1),
//            BookNote("Note Title", "This is content", 0L, "10", true, 1),
//            BookNote("Note Title", "This is content", 0L, "10", false, 1),
//            BookNote("Note Title", "This is content", 0L, "10", true, 1),
//            BookNote("Note Title", "This is content", 0L, "10", true, 1),
//            BookNote("Note Title", "This is content", 0L, "10", false, 1),
//
//            )
//
//        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(view.context)
//        binding.recyclerViewNotes.adapter = NotesAdapter(notes, {})
    }

    private fun setupRecyclerView(){
        notesAdapter= NotesAdapter(viewModel.state.value.notes,{id->
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(1)
            )
        },{/*delete Click*/})
        binding.recyclerViewNotes.apply {
            adapter=notesAdapter
            layoutManager= LinearLayoutManager(context)
        }

    }

    private fun setUpRadioGroup() {
        binding.radioGroupNotes.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbAscending -> {
                    viewModel.onEvent(NotesEvent.Order(NoteOrder.Date(OrderType.Ascending)))
                }

                R.id.rbDescending -> {
                    viewModel.onEvent(NotesEvent.Order(NoteOrder.IsFavorite(OrderType.Descending)))
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionFilterTitle -> {
                viewModel.onEvent(NotesEvent.Order(NoteOrder.Title(OrderType.Descending)))
                true
            }

            R.id.actionFilterDate -> {
                viewModel.onEvent(NotesEvent.Order(NoteOrder.Date(OrderType.Descending)))
                true
            }

            R.id.actionFilterFavorite -> {
                viewModel.onEvent(NotesEvent.Order(NoteOrder.IsFavorite(OrderType.Descending)))
                true
            }

            else -> {
                false
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.booktracking4.presentation.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentNotesBinding
import com.example.booktracking4.databinding.ItemNoteBinding
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

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

        val notes = listOf(
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", false),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 1", "10-20", "This is content", "18.10.2024", true),
            NotesModel(1, "Note 2", "10-20", "This is another content", "18.10.2024", false)
        )

        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerViewNotes.adapter = NotesAdapter(notes,{})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
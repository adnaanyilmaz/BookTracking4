package com.example.booktracking4.presentation.fragments.admin_notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.databinding.FragmentAdminNotesBinding
import com.example.booktracking4.presentation.fragments.admin_notes.adapter.AdminNotesAdapter
import com.example.booktracking4.presentation.fragments.friends_notes.adapter.FriendNotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AdminNotesFragment : Fragment() {
    private var _binding: FragmentAdminNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminNotesViewModel by viewModels()
    private lateinit var adapter: AdminNotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        collectViewModel()

    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                if (state.error.isNotEmpty()) {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                }

                if (state.notesWithUsernames.isNotEmpty()) {
                    adapter.submitData(state.notesWithUsernames)
                } else if (!state.isLoading) {
                    adapter.submitData(emptyList())
                    Toast.makeText(requireContext(), "No public notes found.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        adapter = AdminNotesAdapter(
            onDeleteClick = { note ->
                viewModel.deleteUserPublicNote(note)
                Toast.makeText(requireContext(), "Deleted.", Toast.LENGTH_SHORT)
                    .show()
                collectViewModel()
            }
        )
        binding.rvAdminNotes.adapter = adapter
        binding.rvAdminNotes.addItemDecoration(
            DividerItemDecoration(
                binding.rvAdminNotes.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
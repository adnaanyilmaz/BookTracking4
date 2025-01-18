package com.example.booktracking4.presentation.fragments.friends_notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentFriendNotesBinding
import com.example.booktracking4.presentation.fragments.favorite_books.adapter.FavoriteBooksAdapter
import com.example.booktracking4.presentation.fragments.friends_notes.adapter.FriendNotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendNotesFragment : Fragment() {

    private var _binding: FragmentFriendNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendNotesViewModel by viewModels()
    private lateinit var friendNotesAdapter: FriendNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendNotesBinding.inflate(inflater, container, false)
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
                    friendNotesAdapter.submitData(state.notesWithUsernames)
                } else if (!state.isLoading) {
                    friendNotesAdapter.submitData(emptyList())
                    Toast.makeText(requireContext(), "No public notes found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        friendNotesAdapter = FriendNotesAdapter()
        binding.rvFriendNotes.adapter = friendNotesAdapter
        binding.rvFriendNotes.addItemDecoration(
            DividerItemDecoration(
                binding.rvFriendNotes.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

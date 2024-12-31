package com.example.booktracking4.presentation.fragments.friends_requests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentFriendsRequestsBinding
import com.example.booktracking4.presentation.fragments.friends_requests.adapter.FriendRequestsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FriendsRequestsFragment : Fragment() {

    private var _binding: FragmentFriendsRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendsRequestsViewModel by viewModels()
    private lateinit var adapter: FriendRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Fetch friend requests when the fragment is created
        viewModel.getFriendRequests()
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestsAdapter(
            onAcceptClicked = { requestId ->
                viewModel.acceptFriendRequest(requestId)
            },
            onRejectClicked = { requestId ->
                viewModel.rejectFriendRequest(requestId)
            }
        )
        binding.rvFriendRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FriendsRequestsFragment.adapter
        }
    }

    private fun observeViewModel() {
        // Observe FriendRequestsUiState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.friendRequestsState.collectLatest { state ->
                when (state) {
                    is FriendRequestsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is FriendRequestsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(state.friendRequests)
                    }
                    is FriendRequestsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        // Show error message (e.g., using a Toast or Snackbar)
                    }
                    is FriendRequestsUiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        // Observe AcceptRequestUiState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.acceptRequestState.collectLatest { state ->
                when (state) {
                    is AcceptRequestUiState.Success -> {
                        // Handle success message (e.g., refresh UI or show a Toast)
                    }
                    is AcceptRequestUiState.Error -> {
                        // Handle error message (e.g., show a Snackbar)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.booktracking4.presentation.fragments.friends_requests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private lateinit var adapter: FriendRequestsAdapter
    private val viewModel: FriendsRequestsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        viewModel.getFriendsRequests()
    }

    private fun setupRecyclerView() {
        adapter = FriendRequestsAdapter(
            onAcceptClicked = { senderUserName ->
                viewModel.acceptFriendRequest(senderUserName)
                Toast.makeText(requireContext(), "$senderUserName accepted", Toast.LENGTH_SHORT).show()

            },
            onRejectClicked = { senderUserName ->
                viewModel.rejectFriendRequest(senderUserName)
                Toast.makeText(requireContext(), "$senderUserName rejected", Toast.LENGTH_SHORT).show()

            }
        )
        binding.rvFriendRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter =adapter
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

                    is FriendRequestsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Show error message (e.g., using a Toast or Snackbar)
                    }

                    is FriendRequestsUiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is FriendRequestsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        //Observe RejectRequestUiState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rejectRequestState.collectLatest { state ->
                when (state) {
                    is RejectRequestsUiState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is RejectRequestsUiState.Success -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
            }
        }

        // Observe AcceptRequestUiState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.acceptRequestState.collectLatest { state ->
                when (state) {
                    is AcceptRequestUiState.Success -> {
                        // Handle success message (e.g., refresh UI or show a Toast)
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    is AcceptRequestUiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
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

package com.example.booktracking4.presentation.fragments.friends_requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
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

        setUpRecyclerView()
        collectViewModel()
        observeViewModel()
    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.friendRequestList.isNotEmpty()) {
                    adapter.submitData(state.friendRequestList)
                } else {
                    adapter.submitData(emptyList())
                }
                binding.progressBar.visibility =
                    if (state.isLoading == true) View.VISIBLE else View.GONE

            }
        }
    }


    private fun setUpRecyclerView() {
        adapter = FriendRequestsAdapter(
            onAcceptClicked = {
                viewModel.acceptFriendRequest(senderUserName = it)
                collectViewModel()
            },
            onRejectClicked = {
                viewModel.rejectFriendRequest(senderUserName = it)
                collectViewModel()
            }
        )

        binding.rvFriendRequests.adapter = adapter
        binding.rvFriendRequests.addItemDecoration(
            DividerItemDecoration(
                binding.rvFriendRequests.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeViewModel() {
        // Observe AcceptRequestsUiState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.acceptRequestState.collectLatest { state ->
                when (state) {
                    is AcceptRequestUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    AcceptRequestUiState.Idle -> binding.progressBar.visibility = View.GONE

                    AcceptRequestUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is AcceptRequestUiState.Success ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

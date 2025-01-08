package com.example.booktracking4.presentation.fragments.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentFriendsBinding
import com.example.booktracking4.presentation.fragments.friends.adapter.FriendsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var adapter: FriendsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()


    }

    private fun setupRecyclerView() {
        adapter = FriendsAdapter(onItemClickListener ={uid->
            findNavController().navigate(FriendsFragmentDirections.actionFriendsFragmentToFriendsDetailFragment(uid))
        } )
        binding.rvFriends.adapter = adapter
        binding.rvFriends.addItemDecoration(
            DividerItemDecoration(binding.rvFriends.context, DividerItemDecoration.VERTICAL
            )
        )
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.friendsState.collectLatest { state ->
                when (state) {
                    is FriendsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    FriendsUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is FriendsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitData(state.requests)
                        Toast.makeText(requireContext(),"friends list fetched successfully", Toast.LENGTH_SHORT).show()

                    }

                    else -> {
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
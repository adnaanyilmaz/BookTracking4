package com.example.booktracking4.presentation.fragments.admin_users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.databinding.FragmentAdminUsersBinding
import com.example.booktracking4.presentation.fragments.admin_users.adapter.AdminUserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminUsersFragment : Fragment() {

    private var _binding: FragmentAdminUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminUsersViewModel by viewModels()
    private lateinit var adapter: AdminUserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAdminUsersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }


    private fun setupRecyclerView() {
        adapter = AdminUserAdapter(
            onItemClickListener = { uid ->
                viewModel.deleteUser(uid)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                observeViewModel()
            }
        )
        binding.rvUsers.adapter = adapter
        binding.rvUsers.addItemDecoration(
            DividerItemDecoration(binding.rvUsers.context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userState.collectLatest { state ->
                when (state) {
                    is AdminUserUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    AdminUserUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is AdminUserUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitData(state.requests)
                        Toast.makeText(requireContext(),"Users list fetched successfully", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
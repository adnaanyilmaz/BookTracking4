package com.example.booktracking4.presentation.fragments.friends_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.databinding.FragmentFriendsDetailBinding
import com.example.booktracking4.presentation.fragments.friends_detail.adapter.FriendsDetailAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class FriendsDetailFragment : Fragment() {

    private var _binding: FragmentFriendsDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendsDetailViewModel by viewModels()
    private lateinit var adapter: FriendsDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: FriendsDetailFragmentArgs by navArgs()
        val uid: String = bundle.uid

        viewModel.getUser(uid)
        viewModel.getFriendsBook(uid)
        setupRecyclerView()
        observeViewModel()



    }


    private fun setupRecyclerView() {
        adapter = FriendsDetailAdapter()
        binding.rvFriendsDetail.adapter = adapter
        binding.rvFriendsDetail.addItemDecoration(
            DividerItemDecoration(
                binding.rvFriendsDetail.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is FriendsDetailUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is FriendsDetailUiState.Loading -> if (state.isLoading==true) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
                    is FriendsDetailUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitData(state.requests)
                        Toast.makeText(requireContext(),"book list fetched successfully", Toast.LENGTH_SHORT).show()

                    }

                    else -> {
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userName.collectLatest { state ->
            binding.tvUserName.text=state.userName

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
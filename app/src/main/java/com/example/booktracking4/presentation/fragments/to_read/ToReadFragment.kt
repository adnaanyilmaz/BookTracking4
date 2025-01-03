package com.example.booktracking4.presentation.fragments.to_read

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.databinding.FragmentToReadBinding
import com.example.booktracking4.presentation.fragments.to_read.adapter.ToReadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ToReadFragment : Fragment() {

    private var _binding: FragmentToReadBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<ToReadViewModel>()

    private lateinit var willReadAdapter: ToReadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToReadBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserBooks()
        setUpRecyclerView()
        collectViewModel()
        binding.fabAddBook.setOnClickListener{
            findNavController().navigate(
                ToReadFragmentDirections.actionToReadFragmentToSearchFragment()
            )
        }
    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.toRead.isNotEmpty()) {
                    binding.tvDescription.text = "You will read ${state.toRead.size} books"
                    willReadAdapter.submitData(state.toRead)
                    Log.d("Dante", "Books: ${state.toRead}")
                }else{
                    willReadAdapter.submitData(emptyList())
                    binding.tvDescription.text = "You will read ${state.toRead.size} books"
                }
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setUpRecyclerView() {
        willReadAdapter = ToReadAdapter(
            onItemClickListener = { bookId ->
                findNavController().navigate(
                    ToReadFragmentDirections.actionToReadFragmentToBookDetailFragment(bookId)
                )
            },
            onDeleteClick = { bookId ->
                viewModel.deleteUserBook(bookId)
            },
            onNavigate = {bookName ->
                findNavController().navigate(
                    ToReadFragmentDirections.actionToReadFragmentToAddNoteFragment(0,bookName)
                )
            })
        binding.rvCurrentlyReading.adapter = willReadAdapter
        binding.rvCurrentlyReading.addItemDecoration(
            DividerItemDecoration(
                binding.rvCurrentlyReading.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
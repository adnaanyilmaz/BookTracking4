package com.example.booktracking4.presentation.fragments.to_read

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentToReadBinding
import com.example.booktracking4.presentation.fragments.currently_read.CurrentlyReadingViewModel
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter
import com.example.booktracking4.presentation.fragments.to_read.adapter.ToReadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ToReadFragment : Fragment() {

    private var _binding: FragmentToReadBinding?=null
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
        _binding = FragmentToReadBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserBooks()
        setUpRecyclerView()
        collectViewModel()
    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.willRead.isNotEmpty()) {
                    binding.tvDescription.text = "You will read ${ state.willRead.size } books"
                    willReadAdapter.submitData(state.willRead)
                    Log.d("Dante", "Books: ${state.willRead}")
                }
                if (state.isLoading) {
                    // Progress Bar koy
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        willReadAdapter = ToReadAdapter(onItemClickListener = {})
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
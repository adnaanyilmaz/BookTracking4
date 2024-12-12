package com.example.booktracking4.presentation.fragments.read

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentReadBinding
import com.example.booktracking4.presentation.fragments.read.adapter.ReadAdapter
import com.example.booktracking4.presentation.fragments.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReadFragment : Fragment() {
    private var _binding: FragmentReadBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ReadViewModel>()

    private lateinit var readAdapter: ReadAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
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
                if (state.whatIRead.isNotEmpty()) {
                    binding.tvDescription.text = "You are read ${ state.whatIRead.size } books"
                    readAdapter.submitData(state.whatIRead)
                    Log.d("Dante", "Books: ${state.whatIRead}")
                }
                if (state.isLoading) {
                    // Progress Bar koy
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        readAdapter = ReadAdapter(onItemClickListener = {})
        binding.rvRead.adapter = readAdapter
        binding.rvRead.addItemDecoration(
            DividerItemDecoration(
                binding.rvRead.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
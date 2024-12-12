package com.example.booktracking4.presentation.fragments.currently_read

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
import com.example.booktracking4.databinding.FragmentBookDetailBinding
import com.example.booktracking4.databinding.FragmentCurrentlyReadingBinding
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter
import com.example.booktracking4.presentation.fragments.read.ReadViewModel
import com.example.booktracking4.presentation.fragments.read.adapter.ReadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class CurrentlyReadingFragment : Fragment() {
    private var _binding: FragmentCurrentlyReadingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CurrentlyReadingViewModel>()

    private lateinit var readNowAdapter: CurrentlyReadingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurrentlyReadingBinding.inflate(inflater, container, false)
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
                if (state.readNow.isNotEmpty()) {
                    binding.tvDescription.text = "You are reading ${ state.readNow.size } books"
                        readNowAdapter.submitData(state.readNow)
                    Log.d("Dante", "Books: ${state.readNow}")
                }
                if (state.isLoading) {
                    // Progress Bar koy
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        readNowAdapter = CurrentlyReadingAdapter(onItemClickListener = {})
        binding.rvCurrentlyReading.adapter = readNowAdapter
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
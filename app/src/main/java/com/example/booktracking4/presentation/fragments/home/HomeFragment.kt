package com.example.booktracking4.presentation.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.R
import com.example.booktracking4.common.loadImageView
import com.example.booktracking4.databinding.FragmentHomeBinding
import com.example.booktracking4.domain.model.ui_model.home_model.HomeModel
import com.example.booktracking4.presentation.fragments.currently_read.CurrentlyReadingViewModel
import com.example.booktracking4.presentation.fragments.home.adapter.HomePageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectViewModel()
    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->

                if (state.currentlyReading.isNotEmpty()) {
                    binding.tvCurrentlyReadingTitle.text = state.currentlyReading.last().bookName
                    binding.tvCurrentlyReadingAuthor.text = state.currentlyReading.last().bookName
                    binding.ivCurrentlyReadingCover.loadImageView(state.currentlyReading.last().image)
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
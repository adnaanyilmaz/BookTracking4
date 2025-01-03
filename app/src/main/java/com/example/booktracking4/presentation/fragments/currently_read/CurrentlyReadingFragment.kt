package com.example.booktracking4.presentation.fragments.currently_read

import android.annotation.SuppressLint
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
import com.example.booktracking4.databinding.FragmentCurrentlyReadingBinding
import com.example.booktracking4.presentation.fragments.currently_read.adapter.CurrentlyReadingAdapter
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
        binding.fabAddBook.setOnClickListener{
        findNavController().navigate(
            CurrentlyReadingFragmentDirections.actionCurrentlyReadingFragmentToSearchFragment()
        )
        }
    }

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.currentlyReading.isNotEmpty()) {
                    binding.tvDescription.text = "You are reading ${state.currentlyReading.size} books"
                    readNowAdapter.submitData(state.currentlyReading)
                    Log.d("Dante", "Books: ${state.currentlyReading}")
                }
                else{
                    readNowAdapter.submitData(emptyList())
                    binding.tvDescription.text = "You are reading ${state.currentlyReading.size} books"

                }
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }


    private fun setUpRecyclerView() {
        readNowAdapter = CurrentlyReadingAdapter(
            onItemClickListener = { bookId ->
                findNavController().navigate(
                    CurrentlyReadingFragmentDirections.actionCurrentlyReadingFragmentToBookDetailFragment(
                        bookId
                    )
                )
            }, onDeleteClick = { bookId ->
                viewModel.deleteUserBook(bookId)
            },
            onNavigate ={ bookName->
                findNavController().navigate(
                CurrentlyReadingFragmentDirections.actionCurrentlyReadingFragmentToAddNoteFragment(0,bookName)
            )
            }
        )
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
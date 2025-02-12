package com.example.booktracking4.presentation.fragments.read

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
import com.example.booktracking4.databinding.FragmentReadBinding
import com.example.booktracking4.presentation.fragments.currently_read.CurrentlyReadingFragmentDirections
import com.example.booktracking4.presentation.fragments.read.adapter.ReadAdapter
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchUserBooks()
        setUpRecyclerView()
        collectViewModel()
        binding.fabAddBook.setOnClickListener{
            findNavController().navigate(
                ReadFragmentDirections.actionReadFragmentToSearchFragment()
            )
        }
        //favori filtreleme
//        binding.ivFavoriteFilters.setOnClickListener{
//            viewModel.filterFavoriteBooks()
//            lifecycleScope.launch{
//                viewModel.uiState.collect{state ->
//                    if (state.read.isNotEmpty()) {
//                    readAdapter.submitData(state.read)
//                    binding.tvDescription.text="You have ${state.read.size} favorite books"
//                    } else{
//                        readAdapter.submitData(emptyList())
//                        binding.tvDescription.text="You have ${state.read.size} favorite books"
//                    }
//                }
//            }
//        }


    }

    @SuppressLint("SetTextI18n")
    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.read.isNotEmpty()) {
                    binding.tvDescription.text = "You are read ${state.read.size} books"
                    readAdapter.submitData(state.read)
                    Log.d("Dante", "Books: ${state.read}")
                }else{
                    readAdapter.submitData(emptyList())
                    binding.tvDescription.text = "You are read ${state.read.size} books"
                }
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            }
        }
    }


    private fun setUpRecyclerView() {
        readAdapter = ReadAdapter(
            onItemClickListener = { bookId ->
                findNavController().navigate(
                    ReadFragmentDirections.actionReadFragmentToBookDetailFragment(bookId)
                )
            },
            onDeleteClick = { bookId ->
                viewModel.deleteUserBook(bookId)
            },
            onNavigate = { bookName ->
                findNavController().navigate(
                    ReadFragmentDirections.actionReadFragmentToAddNoteFragment(0,bookName)
                )
            }, onFavoriteClick = {bookId,newFavorite ->
                viewModel.upDateFavorite(bookId = bookId, newIsFavoriteStatus = newFavorite)
//                collectViewModel()
            }
            )
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
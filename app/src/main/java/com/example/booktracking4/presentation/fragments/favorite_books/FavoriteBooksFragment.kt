package com.example.booktracking4.presentation.fragments.favorite_books

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentFavoriteBooksBinding
import com.example.booktracking4.databinding.FragmentFriendsBinding
import com.example.booktracking4.presentation.fragments.favorite_books.adapter.FavoriteBooksAdapter
import com.example.booktracking4.presentation.fragments.read.ReadFragmentDirections
import com.example.booktracking4.presentation.fragments.read.ReadViewModel
import com.example.booktracking4.presentation.fragments.read.adapter.ReadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class FavoriteBooksFragment : Fragment() {

    private var _binding: FragmentFavoriteBooksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoriteBooksViewModel>()
    private lateinit var favoriteBooksAdapter: FavoriteBooksAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFavoriteBooksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        collectViewModel()

    }
    private fun collectViewModel() {
        lifecycleScope.launch{
            viewModel.uiState.collect{state ->
                if (state.read.isNotEmpty()) {
                    favoriteBooksAdapter.submitData(state.read)
                    binding.tvDescription.text="You have ${state.read.size} favorite books"
                    binding.progressBar.visibility= View.GONE
                } else{
                    favoriteBooksAdapter.submitData(emptyList())
                    binding.tvDescription.text="You have ${state.read.size} favorite books"
                    binding.progressBar.visibility= View.GONE
//                    Toast.makeText(requireContext(),"You don't have a favorite book", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        favoriteBooksAdapter = FavoriteBooksAdapter(
            onFavoriteClick = {
                    bookId,newFavorite ->
                viewModel.upDateFavorite(bookId = bookId, newIsFavoriteStatus = newFavorite)
            }
        )
        binding.rvFavoriteBooks.adapter = favoriteBooksAdapter
        binding.rvFavoriteBooks.addItemDecoration(
            DividerItemDecoration(
                binding.rvFavoriteBooks.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
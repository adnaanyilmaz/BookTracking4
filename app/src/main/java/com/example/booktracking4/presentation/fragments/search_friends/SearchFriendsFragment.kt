package com.example.booktracking4.presentation.fragments.search_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentSearchFriendsBinding
import com.example.booktracking4.presentation.fragments.search_friends.adapter.SearchFriendsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFriendsFragment : Fragment() {

    private var _binding: FragmentSearchFriendsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchFriendsViewModel by viewModels()
    private lateinit var adapter: SearchFriendsAdapter // RecyclerView Adapter'ı

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView() // RecyclerView'i kurar
        observeViewModel() // ViewModel akışlarını gözlemler

        // SearchView için kullanıcı adı arama işlemi
        binding.svSearchFriendsPage.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchFriendsByUsername(it) } // ViewModel üzerinden arama işlemi
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false // Text değişikliklerini dinleme
            }
        })
    }

    // RecyclerView'in kurulumu
    private fun setupRecyclerView() {

        adapter = SearchFriendsAdapter(
            onAddFriendClicked = { userName ->
                // Kullanıcı, 'Add Friend' butonuna tıkladığında arkadaş ekleme işlemi başlatılır
                viewModel.sendFriendRequest(receiverUserName = userName)
            },
            sendUid = { uid ->
                viewModel.checkFriendsState(uid = uid)
            },
            onButtonClickState =viewModel.isFriend.value
        )
        observeViewModel()
        binding.rvSearchPage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchPage.adapter = adapter
    }

    // ViewModel'den gelen durumları gözlemleme
    private fun observeViewModel() {
        // Kullanıcı arama durumları
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collectLatest { state ->
                when (state) {
                    is SearchFriendsUiState.Loading -> binding.progressBar.visibility =
                        View.VISIBLE // Yükleniyor
                    is SearchFriendsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(state.users) // Kullanıcıları listele
                    }

                    is SearchFriendsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            .show() // Hata mesajı
                    }

                    SearchFriendsUiState.Idle -> binding.progressBar.visibility = View.GONE
                }
            }
        }

        // Arkadaş ekleme durumları
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addFriendState.collectLatest { state ->
                when (state) {
                    is AddFriendUiState.Loading -> binding.progressBar.visibility =
                        View.VISIBLE // Yükleniyor
                    is AddFriendUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            .show() // Başarı mesajı
                    }

                    is AddFriendUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            .show() // Hata mesajı
                    }

                    AddFriendUiState.Idle -> binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding'in bellekte yer tutmaması için null atanır
    }
}

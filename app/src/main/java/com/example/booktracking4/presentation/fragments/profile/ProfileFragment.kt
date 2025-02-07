package com.example.booktracking4.presentation.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentProfileBinding
import com.example.booktracking4.presentation.fragments.profile.ProfileContract.UiEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOut()
        collectViewModel()

        binding.btnNotification.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToFriendsRequestsFragment()
            )
        }
        binding.btnFavoriteBooks.setOnClickListener{
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToFavoriteBooksFragment()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun collectViewModel() {
        lifecycleScope.launch {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.GoToLoginScreen -> {
                        findNavController().navigate(
                            R.id.action_profileFragment_to_loginFragment,
                            null,
                            navOptions {
                                // NavGraph'in kök öğesine kadar pop işlemi yapar ve tüm back stack'i temizler
                                popUpTo(R.id.main_activity_bottom_navigation) { inclusive = true }
                            }
                        )
                    }


                    is UiEffect.ShowToastMessage -> {
                        Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userState.collect { state ->
                binding.tvUserName.text = state.userName.toString()
                binding.tvUserEmail.text = state.email
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                val readPageCount = state.read.sumOf { it.pageCount ?: 0 }

                binding.tvTotalPages.text=readPageCount.toString()
                binding.tvReadBooksCount.text=state.read.size.toString()
                binding.tvReadingBooksCount.text=state.currentlyReading.size.toString()
            }
        }

    }

    private fun signOut() {
        binding.btnLogoutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
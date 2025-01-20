package com.example.booktracking4.presentation.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOut()
        binding.users.setOnClickListener{
            findNavController().navigate(
                AdminFragmentDirections.actionAdminFragmentToAdminUsersFragment()
            )
        }
    }
    private fun signOut(){
        binding.signOut.setOnClickListener{
            viewModel.signOut()
            findNavController().navigate(

                R.id.action_adminFragment_to_loginFragment,
                null,
                navOptions {
                    // NavGraph'in kök öğesine kadar pop işlemi yapar ve tüm back stack'i temizler
                    popUpTo(R.id.main_activity_bottom_navigation) { inclusive = true }
                }
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.booktracking4.presentation.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.booktracking4.R
import com.example.booktracking4.data.remote.user.User
import com.example.booktracking4.databinding.FragmentRegisterBinding
import com.example.booktracking4.presentation.fragments.register.RegisterContract.RegisterUiEffect
import com.example.booktracking4.presentation.fragments.register.RegisterContract.RegisterUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

        lifecycleScope.launch {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is RegisterUiEffect.ShowToastMessage -> {
                        Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
                    }

                    is RegisterUiEffect.GoToSignInScreen -> {
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUiState.collect { uiState ->
                    handleUiState(uiState)
                }
            }
        }
    }

    private fun handleUiState(uiState: RegisterUiState) {
        if (uiState.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }


    }

    private fun setupListeners() {
        binding.btnSingUp.setOnClickListener {
            val email = binding.etEmailRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val userName = binding.etEmailName.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val uid = viewModel.signUp(email, password)
                    if (uid.isNotEmpty()) {
                        val user = User(
                            uid = uid,
                            userName = userName,
                            email = email,
                        )
                        viewModel.addUser(user = user)
                    }
                    else {
                        // Burada kullanıcı bazı bilgileri boş girdiyse bazı actionlar oluşturulabilir.
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
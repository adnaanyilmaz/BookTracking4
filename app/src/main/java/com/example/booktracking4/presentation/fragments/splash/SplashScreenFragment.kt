package com.example.booktracking4.presentation.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentSplashScreenBinding
import com.example.booktracking4.presentation.fragments.splash.SplashContract.SplashUiEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launch {
            delay(2000)
            viewModel.splashUiEffect.collect { effect ->
                when (effect) {
                    is SplashUiEffect.ShowToast -> {
                        Toast.makeText(requireContext(), effect.message, Toast.LENGTH_SHORT).show()
                    }
                    is SplashUiEffect.GoToSignInScreen -> {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }
                    is SplashUiEffect.GoToMainScreen -> {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                    }

                }
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
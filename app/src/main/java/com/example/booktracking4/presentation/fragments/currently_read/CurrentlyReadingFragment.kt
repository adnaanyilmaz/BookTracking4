package com.example.booktracking4.presentation.fragments.currently_read

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentBookDetailBinding
import com.example.booktracking4.databinding.FragmentCurrentlyReadingBinding


class CurrentlyReadingFragment : Fragment() {
    private var _binding: FragmentCurrentlyReadingBinding? = null
    private val binding get() = _binding!!

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


}
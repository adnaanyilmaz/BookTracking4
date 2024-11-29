package com.example.booktracking4.presentation.fragments.friends_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentFriendsDetailBinding


class FriendsDetailFragment : Fragment() {

    private var _binding: FragmentFriendsDetailBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFriendsDetailBinding.inflate(inflater,container,false)
        return binding.root
    }


}
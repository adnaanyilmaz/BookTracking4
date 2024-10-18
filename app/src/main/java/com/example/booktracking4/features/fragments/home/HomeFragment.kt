package com.example.booktracking4.features.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentHomeBinding
import com.example.booktracking4.domain.model.ui_model.home_model.HomeModel
import com.example.booktracking4.features.fragments.home.adapter.HomePageAdapter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeList: ArrayList<HomeModel> = arrayListOf()
            repeat(20){
                val homeModel=HomeModel("Book Name","Book Author",R.drawable.ic_launcher_foreground)
                homeList.add(homeModel)
            }


        binding.rvHomeList.layoutManager = LinearLayoutManager(view.context)
        binding.rvHomeList.adapter = HomePageAdapter(homeList)

    }


}
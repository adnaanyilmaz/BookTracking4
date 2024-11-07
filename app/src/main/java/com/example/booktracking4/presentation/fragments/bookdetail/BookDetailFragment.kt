package com.example.booktracking4.presentation.fragments.bookdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentBookDetailBinding


class BookDetailFragment : Fragment() {

    private var _binding: FragmentBookDetailBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentBookDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: BookDetailFragmentArgs by navArgs()
        val id: String = bundle.id
        //burada id parametresine ihtiyaç duyan, istek atan metodu çağırmalısın (detay bilgileri için)
        Log.e("Dante","received Id: ${id}")
    }

}
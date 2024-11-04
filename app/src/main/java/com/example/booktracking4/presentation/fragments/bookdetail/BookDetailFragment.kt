package com.example.booktracking4.presentation.fragments.bookdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.booktracking4.R


class BookDetailFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle: BookDetailFragmentArgs by navArgs()
        val id: String = bundle.id
        //burada id parametresine ihtiyaç duyan, istek atan metodu çağırmalısın (detay bilgileri için)
        Log.e("Dante","received Id: ${id}")
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

}
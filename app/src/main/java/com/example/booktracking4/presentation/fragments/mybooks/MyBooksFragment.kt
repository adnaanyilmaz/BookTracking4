package com.example.booktracking4.presentation.fragments.mybooks

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.booktracking4.R
import com.example.booktracking4.databinding.FragmentMyBooksBinding

class MyBooksFragment : Fragment() {
    private var _binding: FragmentMyBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMyBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // MenuProvider ile menüyü ekliyoruz
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menü şişirme işlemi
                menuInflater.inflate(R.menu.menu_my_books_toolbar, menu)

                // SearchView'i buluyoruz
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                // Arama ipucu (hint) ekliyoruz
                searchView.queryHint = "Search in My Books.."

                // Arama işlemi dinleyicisi
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Arama yapıldığında çalışacak kod
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // Arama metni her değiştiğinde çalışacak kod
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Menü öğesi seçimi burada işlenebilir
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

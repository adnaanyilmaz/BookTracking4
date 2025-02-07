package com.example.booktracking4.presentation.fragments.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracking4.databinding.FragmentNotesBinding
import com.example.booktracking4.presentation.fragments.notes.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.booktracking4.R
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


@Suppress("OVERRIDE_DEPRECATION")
@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrder(NoteOrder.Date(OrderType.Descending))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbarNotes.apply {
                // Toolbar'ı aktiviteye bağlamak yerine doğrudan fragment'e bağlarız
                (activity as? AppCompatActivity)?.setSupportActionBar(this)
            }
        }

        setupRecyclerView()
        setupMenu()
        setUpRadioGroup()
        collectUIState()

        viewModel.fetchDistinctBookNames()
        setupSpinner()



    }
    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Menü dosyasını yükle
                    menuInflater.inflate(R.menu.menu_notes, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return onOptionsItemSelected(menuItem)
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onItemClickListener = { note ->
                findNavController().navigate(
                    NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(note.id ?: 1,"")
                )
            },
            onDeleteClick = { note ->
                viewModel.onEvent(NotesEvent.DeleteNote(note))
                showUndoSnackBar(note)
                collectUIState()
            },
          // itemList = viewModel.state.value.notes,
            onFavoriteClick = { note ->
                viewModel.onEvent(NotesEvent.ToggleFavorite(note))
                collectUIState()
            }
        )
        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }


    private fun NotesFragment.showUndoSnackBar(note: BookNote) {
        Snackbar.make(
            binding.root, "${note.title} Deleted", Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            viewModel.onEvent(NotesEvent.RestoreNote)
            collectUIState()

        }.show()
    }


    private fun setUpRadioGroup() {
        binding.radioGroupNotes.setOnCheckedChangeListener { _, checkedId ->
            val noteOrder = when (checkedId) {
                R.id.rbAscending -> NoteOrder.Date(OrderType.Ascending)

                R.id.rbDescending -> NoteOrder.Date(OrderType.Descending)
                else -> return@setOnCheckedChangeListener
            }
            viewModel.onEvent(NotesEvent.Order(noteOrder))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val selectedRadioButton = getSelectedRadioButtonText()

        // Seçili düzenleme tipini belirle
        val noteOrder = when (selectedRadioButton) {
            "Ascending" -> OrderType.Ascending
            "Descending" -> OrderType.Descending
            else -> OrderType.Descending // Varsayılan değer
        }

        // Menü öğesine göre ilgili filtreyi uygula
        val event = when (item.itemId) {
            R.id.actionFilterTitle -> NotesEvent.Order(NoteOrder.Title(noteOrder))
            R.id.actionFilterDate -> NotesEvent.Order(NoteOrder.Date(noteOrder))
            R.id.actionFilterFavorite -> NotesEvent.Order(NoteOrder.IsFavorite(noteOrder))
            else -> return false
        }

        // ViewModel ile olayı tetikle ve UI güncelle
        viewModel.onEvent(event)
        collectUIState()
        return true
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun collectUIState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                notesAdapter.submitList(state.notes) // Listeyi güncelle
            }
        }
    }


    private fun getSelectedRadioButtonText(): String {
        val checkedId = binding.radioGroupNotes.checkedRadioButtonId
        if (checkedId != -1) { // Eğer bir seçim yapılmışsa
            val selectedRadioButton = binding.radioGroupNotes.findViewById<RadioButton>(checkedId)
            return selectedRadioButton?.text.toString()
        }
        return "null"
    }

    private fun setupSpinner() {
        lifecycleScope.launch{
        viewModel.state.collect { state ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                state.bookNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
        }

        // Varsayılan olarak hiçbir seçim yapılmamasını sağla
        binding.spinner.setSelection(0)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBookName = parent?.getItemAtPosition(position).toString()
                if (selectedBookName != "Please select") {
                    viewModel.filterNotesByBookName(selectedBookName)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlem
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
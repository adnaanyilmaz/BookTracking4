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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupMenu()
        setupRadioGroup()
        collectUIState()
        observeSyncState()

        // Kullanıcının işlem yapmasına gerek kalmadan Firebase senkronizasyonu başlat
    }

    private fun setupToolbar() {
        binding.toolbarNotes.apply {
            (activity as? AppCompatActivity)?.setSupportActionBar(this)
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_notes, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return onOptionsItemSelected(menuItem)
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun setupRecyclerView() {
        // Adapteri boş bir listeyle başlatıyoruz
        notesAdapter = NotesAdapter(
            itemList = mutableListOf(),
            onItemClickListener = { note ->
                findNavController().navigate(
                    NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(note.id ?: 0, "")
                )
            },
            onDeleteClick = { note ->
                viewModel.onEvent(NotesEvent.DeleteNote(note))
                showUndoSnackBar(note)
            },
            onFavoriteClick = { note ->
                viewModel.onEvent(NotesEvent.ToggleFavorite(note))
            }
        )

        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showUndoSnackBar(note: BookNote) {
        Snackbar.make(binding.root, "${note.title} Deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.onEvent(NotesEvent.RestoreNote)
            }.show()
    }

    private fun setupRadioGroup() {
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

        val noteOrder = when (selectedRadioButton) {
            "Ascending" -> OrderType.Ascending
            "Descending" -> OrderType.Descending
            else -> OrderType.Descending
        }

        val event = when (item.itemId) {
            R.id.actionFilterTitle -> NotesEvent.Order(NoteOrder.Title(noteOrder))
            R.id.actionFilterDate -> NotesEvent.Order(NoteOrder.Date(noteOrder))
            R.id.actionFilterFavorite -> NotesEvent.Order(NoteOrder.IsFavorite(noteOrder))
            else -> return false
        }

        viewModel.onEvent(event)
        return true
    }

    private fun observeSyncState() {
        lifecycleScope.launchWhenStarted {
            viewModel.syncState.collect { state ->
                when (state) {
                    is SyncState.Syncing -> showLoadingIndicator()
                    is SyncState.Success -> {
                        hideLoadingIndicator()
                        Snackbar.make(binding.root, "Notes synced successfully!", Snackbar.LENGTH_SHORT).show()
                    }
                    is SyncState.Error -> {
                        hideLoadingIndicator()
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                    is SyncState.Idle -> hideLoadingIndicator()
                }
            }
        }
    }

    private fun collectUIState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                notesAdapter.updateItems(state.notes)
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.visibility = View.GONE
    }

    private fun getSelectedRadioButtonText(): String {
        val checkedId = binding.radioGroupNotes.checkedRadioButtonId
        return if (checkedId != -1) {
            binding.radioGroupNotes.findViewById<RadioButton>(checkedId)?.text.toString()
        } else {
            "null"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

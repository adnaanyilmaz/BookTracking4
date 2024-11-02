package com.example.booktracking4.presentation.fragments.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel
import com.example.booktracking4.domain.usecase.BookNoteUseCases
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType
import com.example.booktracking4.presentation.fragments.search.BookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: BookNoteUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(BookNoteState())
    val state: StateFlow<BookNoteState> = _state

    private var recentlyDeleteNote: BookNote? = null

    private var getNotesJob: Job? = null

    init {
        getOrder(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }

            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteBookNoteUseCase(event.note)
                }
                recentlyDeleteNote = event.note

            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }

            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSelectionVisible = !state.value.isOrderSelectionVisible
                )

            }
        }
    }

    val notesLiveData = MutableLiveData<NotesModel>()

    fun getData() {
        val note = NotesModel(1, "Note 1", "25-35", "This is content", "18.10.2024", true)
        notesLiveData.value = note
    }

    private fun getOrder(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getBookNoteUseCase(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes=notes,
                noteOrder=noteOrder
            )
        }
            .launchIn(viewModelScope)

    }
}
package com.example.booktracking4.presentation.fragments.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.NotesRepository
import com.example.booktracking4.domain.usecase.note_use_cases.BookNoteUseCases
import com.example.booktracking4.domain.util.NoteOrder
import com.example.booktracking4.domain.util.OrderType
import com.google.firebase.auth.FirebaseAuth
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
    private val noteUseCases: BookNoteUseCases,
    private val firebaseRepository: NotesRepository
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
                getOrder(event.noteOrder)

            }

            is NotesEvent.DeleteNote -> {
                recentlyDeleteNote = event.note
                viewModelScope.launch {
                    noteUseCases.deleteBookNoteUseCase(event.note)
                    getOrder(NoteOrder.Date(OrderType.Descending))
                }

            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeleteNote ?: return@launch)
                    firebaseRepository.insertNote(recentlyDeleteNote ?: BookNote())
                    recentlyDeleteNote = null
                    getOrder(NoteOrder.Date(OrderType.Descending))
                }

            }

            is NotesEvent.ToggleFavorite -> {
                viewModelScope.launch{
                    noteUseCases.updateNote(event.note)
                    getOrder(NoteOrder.Date(OrderType.Descending))
                }

            }
        }
    }

    fun getOrder(noteOrder: NoteOrder) {
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
package com.example.booktracking4.presentation.fragments.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.repository.NotesRepository
import com.example.booktracking4.domain.usecase.note_use_cases.BookNoteUseCases
import com.example.booktracking4.domain.util.NoteOrder
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
    private val notesRepository: NotesRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState

    private val _state = MutableStateFlow(BookNoteState())
    val state: StateFlow<BookNoteState> = _state

    private var recentlyDeletedNote: BookNote? = null
    private var getNotesJob: Job? = null

    init {
        loadUserNotes() // Kullanıcının notlarını yükle
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
                viewModelScope.launch {
                    noteUseCases.deleteBookNoteUseCase(event.note)
                    recentlyDeletedNote = event.note
                    loadUserNotes() // Silme işleminden sonra notları güncelle
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                    loadUserNotes() // Geri yükleme işleminden sonra notları güncelle
                }
            }
            is NotesEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    noteUseCases.updateNote(event.note.copy(isFavorite = !event.note.isFavorite))
                    loadUserNotes() // Favori durumu güncellenince notları güncelle
                }
            }
        }
    }

    private fun getOrder(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getBookNoteUseCase(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
            syncNotesToFirebase() // Notları Firebase ile senkronize et
        }.launchIn(viewModelScope)
    }

    fun syncNotesToFirebase() {
        val user = auth.currentUser
        if (user == null) {
            Log.e("FirebaseSync", "Kullanıcı oturum açmamış. Senkronizasyon iptal edildi.")
            return
        }

        val uid = user.uid
        viewModelScope.launch {
            _syncState.value = SyncState.Syncing
            try {
                notesRepository.syncNotesWithFirebase(uid, _state.value.notes)
                _syncState.value = SyncState.Success
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Senkronizasyon hatası: ${e.message}")
                _syncState.value = SyncState.Error("Senkronizasyon hatası: ${e.message}")
            }
        }
    }

    private fun loadUserNotes() {
        val user = auth.currentUser
        if (user == null) {
            Log.e("NotesViewModel", "Kullanıcı oturum açmamış. Notlar yüklenemedi.")
            return
        }

        val uid = user.uid
        viewModelScope.launch {
            getNotesJob?.cancel()
            getNotesJob = notesRepository.getAllNotesFromRoom(uid).onEach { notes ->
                _state.value = state.value.copy(notes = notes)
            }.launchIn(viewModelScope)
        }
    }
}

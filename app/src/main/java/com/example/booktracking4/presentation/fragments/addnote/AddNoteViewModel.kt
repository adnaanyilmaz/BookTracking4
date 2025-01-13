package com.example.booktracking4.presentation.fragments.addnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.data.data_resourse.NoteDao
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.model.room.InvalidNoteException
import com.example.booktracking4.domain.repository.NotesRepository
import com.example.booktracking4.domain.usecase.note_use_cases.BookNoteUseCases
import com.example.booktracking4.presentation.fragments.addnote.AddNoteViewModel.UiEvent.*
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: BookNoteUseCases,
    private val auth: FirebaseAuth,
    private val firebaseRepository: NotesRepository,
    private val noteDao: NoteDao

) : ViewModel() {
    private val _noteTitle = MutableStateFlow(NoteTextFieldState())
    val noteTitle: StateFlow<NoteTextFieldState> = _noteTitle

    private val _noteContent = MutableStateFlow(NoteTextFieldState())
    val noteContent: StateFlow<NoteTextFieldState> = _noteContent

    private val _pageCount = MutableStateFlow(NoteTextFieldState())
    val pageCount: StateFlow<NoteTextFieldState> = _pageCount

    private val _bookName = MutableStateFlow(NoteTextFieldState())
    val bookName: StateFlow<NoteTextFieldState> = _bookName

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _id = MutableStateFlow(0)
    val id: StateFlow<Int> = _id

    private val _isPrivateStatus = MutableStateFlow(true)
    val isPrivateStatus: StateFlow<Boolean> = _isPrivateStatus

    fun updatePrivateStatus(newStatus: Boolean) {
        _isPrivateStatus.value = newStatus
    }

    fun updateData(newData: Int) {
        _id.value = newData
    }


    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private var currentNoteId: Int? = null


    fun loadNoteDetails(noteId: Int?) {
        if (noteId != 0 && noteId != null) {
            viewModelScope.launch {
                noteUseCases.editNote(noteId)?.let { note ->
                    currentNoteId = note.id
                    _noteTitle.value = _noteTitle.value.copy(
                        text = note.title,
                    )
                    _noteContent.value = _noteContent.value.copy(
                        text = note.content,
                    )
                    _pageCount.value = _pageCount.value.copy(
                        text = note.page.toString(),
                    )
                    _bookName.value = _bookName.value.copy(
                        text = note.bookName.toString(),
                    )

                    _isFavorite.value = note.isFavorite
                }
            }
        }

    }


    fun onEvent(event: AddNoteEvent) {
        when (event) {
            is AddNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.BookName -> {
                _bookName.value = bookName.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.EnteredPageNumber -> {
                _pageCount.value = pageCount.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.isFavorite -> {
                _isFavorite.value = isFavorite.value
            }

            is AddNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            BookNote(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                page = pageCount.value.text,
                                isFavorite = isFavorite.value,
                                id = currentNoteId,
                                bookName = bookName.value.text,
                                userId = auth.currentUser?.uid!!,
                                status = isPrivateStatus.value
                            )
                        )
                        val result = firebaseRepository.getNoteById(_id.value)
                        if (currentNoteId==null) {
                            firebaseRepository.insertNote(noteDao.getLastNote())
                        } else if (result != null) {
                            firebaseRepository.updateNote(BookNote(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                page = pageCount.value.text,
                                isFavorite = isFavorite.value,
                                id = result.id,
                                bookName = bookName.value.text,
                                userId = auth.currentUser?.uid!!,
                                status = _isPrivateStatus.value
                            ))
                        }
                        _eventFlow.emit(SaveNote)
                    } catch (e: InvalidNoteException) {
                        ShowSnackBar(
                            message = e.message ?: "Couldn't save note"
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()

    }

    fun updateBookName(newBookName: String) {
        _bookName.value = NoteTextFieldState(text = newBookName)
    }
}
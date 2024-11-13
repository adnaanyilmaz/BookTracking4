package com.example.booktracking4.presentation.fragments.addnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracking4.domain.model.room.BookNote
import com.example.booktracking4.domain.model.room.InvalidNoteException
import com.example.booktracking4.domain.usecase.note_use_cases.BookNoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: BookNoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteTitle = MutableStateFlow(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: StateFlow<NoteTextFieldState> = _noteTitle

    private val _noteContent = MutableStateFlow(
        NoteTextFieldState(
            hint = "Enter some content"
        )
    )
    val noteContent: StateFlow<NoteTextFieldState> = _noteContent

    private val _pageCount = MutableStateFlow(NoteTextFieldState())
    val pageCount: StateFlow<NoteTextFieldState> = _pageCount

    private val _isFavorite = MutableStateFlow(true)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNodeId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId").let { noteId ->
            if(noteId != -1){
              viewModelScope.launch{
                  noteUseCases.editNote(noteId)?.also { note ->
                      currentNodeId=note.id
                      _noteTitle.value=noteTitle.value.copy(
                          text = note.title,
                          isHintVisible = false
                      )
                      _noteContent.value=noteContent.value.copy(
                          text = note.content,
                          isHintVisible = false
                      )
                      _pageCount.value=pageCount.value.copy(
                          text = note.title,
                          isHintVisible = false
                      )
                      _isFavorite.value=isFavorite.value
                  }
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

            is AddNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.hasFocus && noteTitle.value.text.isBlank()
                )
            }

            is AddNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.hasFocus && noteContent.value.text.isBlank()
                )
            }

            is AddNoteEvent.EnteredPageNumber -> {
                _pageCount.value = pageCount.value.copy(
                    text = event.value
                )
            }

            is AddNoteEvent.ChangePageNumberFocus -> {
                _pageCount.value = pageCount.value.copy(
                    isHintVisible = !event.hasFocus &&
                            pageCount.value.text.isBlank()
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
                                id = currentNodeId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        UiEvent.ShowSnackBar(
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


}
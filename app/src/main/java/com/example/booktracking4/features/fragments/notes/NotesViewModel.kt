package com.example.booktracking4.features.fragments.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.booktracking4.domain.model.ui_model.notes_model.NotesModel

class NotesViewModel: ViewModel() {
    val notesLiveData=MutableLiveData<NotesModel>()

    fun getData(){
        val note=NotesModel(1,"Note 1","25-35","This is content","18.10.2024",true)
        notesLiveData.value=note
    }
}
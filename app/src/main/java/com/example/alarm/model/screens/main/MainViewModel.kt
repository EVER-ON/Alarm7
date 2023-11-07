package com.example.alarm.model.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.alarm.App
import com.example.alarm.model.Note

class MainViewModel : ViewModel() {
    val noteListLiveData: LiveData<List<Note>> = App.getInstance().getNoteDao().getAllLiveData()
}

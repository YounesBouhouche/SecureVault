package com.younesb.securevault.features.main.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.NewItemAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    fun showFilePicker(action: NewItemAction) {
        viewModelScope.launch {
            MainEventsBus.sendEvent(
                when(action) {
                    NewItemAction.IMPORT -> MainEvent.PickFile
                    NewItemAction.GALLERY -> MainEvent.PickPicture
                    NewItemAction.CAMERA -> MainEvent.TakePicture
                    NewItemAction.NOTE -> MainEvent.RequestNewNote
                }
            )
        }
    }
}
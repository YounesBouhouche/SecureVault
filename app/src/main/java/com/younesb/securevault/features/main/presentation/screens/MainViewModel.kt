package com.younesb.securevault.features.main.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.export.presentation.util.ExportEvent
import com.younesb.securevault.features.export.presentation.util.ExportEventsBus
import com.younesb.securevault.features.main.domain.usecases.CreateFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.CreateTagUseCase
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.NewItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    val createFolderUseCase: CreateFolderUseCase,
    val createTagUseCase: CreateTagUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.ShowFilePicker -> {
                if (action.type == NewItemType.FOLDER) {
                    _uiState.update {
                        it.copy(showNewFolderDialog = true)
                    }
                    return
                }
                viewModelScope.launch {
                    MainEventsBus.sendEvent(
                        when (action.type) {
                            NewItemType.FILE -> MainEvent.PickFile
                            NewItemType.GALLERY -> MainEvent.PickPicture
                            NewItemType.CAMERA -> MainEvent.TakePicture
                            NewItemType.NOTE -> MainEvent.RequestNewNote
                        }
                    )
                }
            }

            Action.HideNewFolderDialog -> _uiState.update {
                it.copy(showNewFolderDialog = false)
            }
            Action.ShowNewFolderDialog -> _uiState.update {
                it.copy(showNewFolderDialog = true)
            }
            is Action.CreateFolder -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(newFolderLoading = true)
                    }
                    createFolderUseCase(name = action.name)
                    _uiState.update {
                        it.copy(newFolderLoading = false, showNewFolderDialog = false)
                    }
                }
            }

            Action.HideNewTagDialog -> _uiState.update {
                it.copy(showNewTagDialog = false)
            }
            Action.ShowNewTagDialog -> _uiState.update {
                it.copy(showNewTagDialog = true)
            }
            is Action.CreateTag -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(newTagLoading = true)
                    }
                    createTagUseCase(name = action.name)
                    _uiState.update {
                        it.copy(newTagLoading = false, showNewTagDialog = false)
                    }
                }
            }

            is Action.ShowExportSheet -> {
                viewModelScope.launch {
                    ExportEventsBus.sendEvent(ExportEvent.ShowExportSheet(action.ids))
                }
            }
        }
    }

}
package com.younesb.securevault.features.main.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onError
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.main.domain.usecases.CreateFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.CreateTagUseCase
import com.younesb.securevault.features.main.domain.usecases.ExportDocumentsUseCase
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.NewItemType
import com.younesb.securevault.features.main.presentation.util.SaveFileDialogManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(
    val createFolderUseCase: CreateFolderUseCase,
    val createTagUseCase: CreateTagUseCase,
    val exportDocumentsUseCase: ExportDocumentsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.ShowFilePicker -> {
                viewModelScope.launch {
                    MainEventsBus.sendEvent(
                        when (action.type) {
                            NewItemType.IMPORT -> MainEvent.PickFile
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
                onShowExportSheet(action.ids)
            }
            Action.HideExportSheet -> {
                _uiState.update {
                    it.copy(exportState = ExportState.Initial)
                }
            }

            is Action.ExportFiles -> {
                onExportFiles(action.encrypted)
            }
        }
    }

    fun onShowExportSheet(ids: List<String>? = null) {
        _uiState.update {
            it.copy(exportState = ExportState.Idle(ids))
        }
    }

    fun onExportFiles(encrypted: Boolean) {
        val state = _uiState.value.exportState as? ExportState.Idle ?: return
        _uiState.update {
            it.copy(exportState = ExportState.Exporting)
        }
        viewModelScope.launch {
            MainEventsBus.sendEvent(MainEvent.PickSavePath("export_${LocalDateTime.now()}.zip"))
            val uri = SaveFileDialogManager.results.first()
            exportDocumentsUseCase(
                documents = state.ids,
                destination = uri,
                encrypted = encrypted
            ).onSuccess {
                _uiState.update {
                    it.copy(exportState = ExportState.Success)
                }
            }.onError { error ->
                error.printStackTrace()
                _uiState.update {
                    it.copy(exportState = ExportState.Error(error.message ?: ""))
                }
            }
        }
    }
}
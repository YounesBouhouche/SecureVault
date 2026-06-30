package com.younesb.securevault.features.main.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.usecases.CreateFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.CreateTagUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveTagsUseCase
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.NewItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.text.contains

class MainViewModel(
    val createFolderUseCase: CreateFolderUseCase,
    val createTagUseCase: CreateTagUseCase,
    observeTagsUseCase: ObserveTagsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedTags = MutableStateFlow(emptySet<String>())
    val selectedTags = _selectedTags.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )

    val tags = observeTagsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

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
            is Action.ToggleTagSelection -> {
                _selectedTags.update { oldSet ->
                    if (action.id in oldSet) oldSet + action.id else oldSet - action.id
                }
            }
        }
    }
}
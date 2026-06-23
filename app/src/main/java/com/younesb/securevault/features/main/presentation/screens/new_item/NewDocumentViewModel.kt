package com.younesb.securevault.features.main.presentation.screens.new_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveTagsUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveNoteUseCase
import com.younesb.securevault.features.main.presentation.NewDocument
import com.younesb.securevault.features.main.presentation.util.FilePickerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewDocumentViewModel(
    val saveDocumentUseCase: SaveDocumentUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
    observeFoldersUseCase: ObserveFoldersUseCase,
    observeTagsUseCase: ObserveTagsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewDocumentUiState())
    private val _folders = observeFoldersUseCase()
    private val _tags = observeTagsUseCase()
    val uiState = combine(_uiState, _folders, _tags) { state, folders, tags ->
        state.copy(folders = folders, tags = tags)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        NewDocumentUiState()
    )

    init {
        viewModelScope.launch {
            FilePickerManager.fileResults.collectLatest {
                println(it)
                val fileUri = when (it) {
                    is NewDocument.File -> it.uri
                    is NewDocument.Image -> it.uri
                    is NewDocument.Note -> null
                }
                val name = fileUri?.lastPathSegment ?: ""
                val type = when (it) {
                    is NewDocument.File -> DocumentType.FILE
                    is NewDocument.Image -> DocumentType.IMAGE
                    is NewDocument.Note -> DocumentType.NOTE
                }
                _uiState.update { state ->
                    state.copy(
                        fileUri = fileUri,
                        name = name,
                        type = type,
                        sheetVisible = true
                    )
                }
            }
        }
    }

    fun onAction(action: NewDocumentAction) {
        when (action) {
            is NewDocumentAction.Confirm -> {
                val folderId = _uiState.value.selectedFolder?.let {
                    uiState.value.folders.getOrNull(it)
                }?.id ?: ""
                val document = DocumentDto(
                    name = action.name,
                    type = _uiState.value.type,
                    folderId = folderId,
                    tags = emptyList()
                )
                viewModelScope.launch {
                    (if (_uiState.value.type == DocumentType.NOTE) {
                        saveNoteUseCase(
                            content = action.noteContent,
                            document = document
                        )
                    } else {
                        saveDocumentUseCase(
                            sourceFile = _uiState.value.fileUri ?: return@launch,
                            document = document
                        )
                    }).onSuccess {
                        _uiState.update {
                            it.copy(sheetVisible = false)
                        }
                    }
                }
            }

            NewDocumentAction.Dismiss -> {
                _uiState.update { state ->
                    state.copy(sheetVisible = false)
                }
            }

            is NewDocumentAction.SelectFolder -> {
                _uiState.update { state ->
                    state.copy(selectedFolder = action.index)
                }
            }

            is NewDocumentAction.SetTagSelected -> {
                _uiState.update { state ->
                    val updatedTags = if (action.selected) {
                        state.selectedTags + action.id
                    } else {
                        state.selectedTags - action.id
                    }
                    state.copy(selectedTags = updatedTags)
                }
            }
        }
    }
}
package com.younesb.securevault.features.main.presentation.screens.new_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.usecases.SaveDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveNoteUseCase
import com.younesb.securevault.features.main.presentation.NewDocument
import com.younesb.securevault.features.main.presentation.util.FilePickerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewDocumentViewModel(
    val saveDocumentUseCase: SaveDocumentUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(NewDocumentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            FilePickerManager.fileResults.collectLatest {
                println(it)
                val fileUri = when(it) {
                    is NewDocument.File -> it.uri
                    is NewDocument.Image -> it.uri
                    is NewDocument.Note -> null
                }
                val name = fileUri?.lastPathSegment ?: ""
                val type = when(it) {
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
        when(action) {
            is NewDocumentAction.Confirm -> {
                val document = DocumentDto(
                    name = action.name,
                    type = _uiState.value.type,
                    folderId = null,
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
        }
    }
}
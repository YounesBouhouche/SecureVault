package com.younesb.securevault.features.main.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.core.domain.utils.onError
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.export.presentation.util.ExportEvent.ShowExportSheet
import com.younesb.securevault.features.export.presentation.util.ExportEventsBus
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.usecases.CheckForFileExistence
import com.younesb.securevault.features.main.domain.usecases.CreateFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SaveNoteUseCase
import com.younesb.securevault.features.main.presentation.NewDocument
import com.younesb.securevault.features.main.presentation.components.new_doc.DocNameError
import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocState
import com.younesb.securevault.features.main.presentation.util.FileInfo
import com.younesb.securevault.features.main.presentation.util.FilePickerManager
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.NewItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    val createFolderUseCase: CreateFolderUseCase,
    val saveDocumentUseCase: SaveDocumentUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
    val checkForFileExistence: CheckForFileExistence
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var newDocInfo: FileInfo? = null

    init {
        viewModelScope.launch {
            FilePickerManager.fileResults.collectLatest { newDoc ->
                when(newDoc) {
                    is NewDocument.Folder,
                    is NewDocument.Note -> return@collectLatest
                    else -> {}
                }
                val fileUri = when (newDoc) {
                    is NewDocument.File -> newDoc.uri
                    is NewDocument.Image -> newDoc.uri
                }
                val name = newDoc.info?.name ?: fileUri.lastPathSegment ?: ""
                val info = newDoc.info?.copy(name = name)
                newDocInfo = info
                if (_uiState.value.newDocState is NewDocState.Expanded) {
                    _uiState.update { state ->
                        state.copy(
                            newDocState = when (newDoc) {
                                is NewDocument.File -> NewDocState.File(fileUri, name)
                                is NewDocument.Image -> NewDocState.Photo(fileUri, name)
                            },
                            newDocName = name
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.ShowExportSheet -> {
                viewModelScope.launch {
                    ExportEventsBus.sendEvent(ShowExportSheet(action.ids))
                }
            }

            is Action.UpdateNewDocState -> {
                if (action.newDocState is NewDocState.New) {
                    when(action.newDocState.type) {
                        NewItemType.GALLERY -> {
                            viewModelScope.launch {
                                MainEventsBus.sendEvent(MainEvent.PickPicture)
                            }
                            return
                        }
                        NewItemType.FILE -> {
                            viewModelScope.launch {
                                MainEventsBus.sendEvent(MainEvent.PickFile)
                            }
                            return
                        }
                        NewItemType.FOLDER -> {
                            _uiState.update {
                                it.copy(newDocState = NewDocState.Folder)
                            }
                            return
                        }
                        NewItemType.NOTE -> {
                            _uiState.update {
                                it.copy(newDocState = NewDocState.Note)
                            }
                            return
                        }
                        else -> {}
                    }
                }
                _uiState.update {
                    it.copy(newDocState = action.newDocState)
                }
            }

            is Action.CreateNewDoc -> {
                viewModelScope.launch {
                    if (action.name.isBlank()) {
                        _uiState.update {
                            it.copy(docNameError = DocNameError.EMPTY)
                        }
                        return@launch
                    }
                    if (action.name.length > 255) {
                        _uiState.update {
                            it.copy(docNameError = DocNameError.TOO_LONG)
                        }
                        return@launch
                    }
                    if ((checkForFileExistence(action.name) as? Result.Success)?.data == true) {
                        _uiState.update {
                            it.copy(docNameError = DocNameError.ALREADY_EXISTS)
                        }
                        return@launch
                    }
                    val state = _uiState.value.newDocState
                    val docType = when(state) {
                        NewDocState.Note -> DocumentType.NOTE
                        is NewDocState.Photo -> DocumentType.IMAGE
                        else -> DocumentType.FILE
                    }

                    val document = DocumentDto(
                        name = action.name,
                        type = docType,
                        mimeType = newDocInfo?.mimeType ?: ""
                    )
                    (when (state) {
                        NewDocState.Note -> {
                            saveNoteUseCase(
                                content = action.description,
                                document = document
                            )
                        }
                        NewDocState.Folder -> {
                            createFolderUseCase(name = action.name)
                        }
                        is NewDocState.File -> {
                            saveDocumentUseCase(
                                sourceFile = state.uri,
                                document = document
                            )
                        }
                        is NewDocState.Photo -> {
                            saveDocumentUseCase(
                                sourceFile = state.uri,
                                document = document
                            )
                        }
                        else -> return@launch
                    }).onSuccess { fileDeleted ->
                        _uiState.update {
                            it.copy(newDocState = NewDocState.Collapsed)
                        }
                    }.onError {
                        it.printStackTrace()
                    }
                }
            }
        }
    }
}
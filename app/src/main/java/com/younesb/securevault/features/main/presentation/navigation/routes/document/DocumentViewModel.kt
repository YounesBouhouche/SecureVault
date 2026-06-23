package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.usecases.DeleteDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.GetDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.OpenDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.RenameDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.SetFavoriteUseCase
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import com.younesb.securevault.features.main.presentation.util.Resource
import com.younesb.securevault.features.main.presentation.util.getOrNull
import com.younesb.securevault.features.main.presentation.util.map
import com.younesb.securevault.features.main.presentation.util.toResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DocumentViewModel(
    getDocumentUseCase: GetDocumentUseCase,
    openDocumentUseCase: OpenDocumentUseCase,
    val setFavoriteUseCase: SetFavoriteUseCase,
    val renameDocumentUseCase: RenameDocumentUseCase,
    val deleteDocumentUseCase: DeleteDocumentUseCase,
    documentId: String,
): ViewModel() {
    private val _document = MutableStateFlow<Resource<DocumentDto, Throwable>>(Resource.Idle)
    val document = _document.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Resource.Idle)

    private val _file = MutableStateFlow<Resource<Any, Throwable>>(Resource.Idle)
    val file = _file.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Resource.Idle)

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    init {
        viewModelScope.launch {
            launch {
                _document.value = Resource.Loading
                _file.value = Resource.Loading
                _document.value = getDocumentUseCase(documentId).toResource().also { doc ->
                    val result = openDocumentUseCase(documentId)
                    _file.value = result.toResource().map {
                        if (doc.getOrNull()?.type == DocumentType.NOTE)
                            it.decodeToString()
                        else it
                    }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.ToggleFavorite -> {
                val id = _document.value.getOrNull()?.id ?: return
                val isFavorite = !_uiState.value.isFavorite
                _uiState.update {
                    it.copy(isFavorite = isFavorite)
                }
                viewModelScope.launch {
                    setFavoriteUseCase(id, isFavorite)
                        .onSuccess {
                            _document.update { doc ->
                                doc.map {
                                    it.copy(favorite = isFavorite)
                                }
                            }
                        }
                }
            }

            Action.Delete -> {
                val id = _document.value.getOrNull()?.id ?: return
                _uiState.update {
                    it.copy(deleteDialogVisible = false)
                }
                viewModelScope.launch {
                    deleteDocumentUseCase(id)
                        .onSuccess {
                            MainEventsBus.sendEvent(MainEvent.MainPopBackStack)
                        }
                }
            }
            Action.HideDeleteDialog -> {
                _uiState.update {
                    it.copy(deleteDialogVisible = false)
                }
            }
            Action.HideRenameDialog -> {
                _uiState.update {
                    it.copy(renameDialogVisible = false)
                }
            }
            is Action.Rename -> {
                val id = _document.value.getOrNull()?.id ?: return
                _uiState.update {
                    it.copy(renameDialogVisible = false)
                }
                viewModelScope.launch {
                    renameDocumentUseCase(id, action.newName)
                        .onSuccess {
                            _document.update { doc ->
                                doc.map {
                                    it.copy(name = action.newName)
                                }
                            }
                        }
                }
            }
            Action.ShowDeleteDialog -> {
                _uiState.update {
                    it.copy(deleteDialogVisible = true)
                }
            }
            Action.ShowRenameDialog -> {
                _uiState.update {
                    it.copy(renameDialogVisible = true)
                }
            }
        }
    }

}
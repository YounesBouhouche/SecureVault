package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.usecases.GetDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.OpenDocumentUseCase
import com.younesb.securevault.features.main.presentation.util.Resource
import com.younesb.securevault.features.main.presentation.util.getOrNull
import com.younesb.securevault.features.main.presentation.util.map
import com.younesb.securevault.features.main.presentation.util.toResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocumentViewModel(
    getDocumentUseCase: GetDocumentUseCase,
    openDocumentUseCase: OpenDocumentUseCase,
    documentId: String,
): ViewModel() {
    private val _document = MutableStateFlow<Resource<DocumentDto, Throwable>>(Resource.Idle)
    val document = _document.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Resource.Idle)

    private val _file = MutableStateFlow<Resource<Any, Throwable>>(Resource.Idle)
    val file = _file.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Resource.Idle)

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
            else -> Unit
        }
    }

}
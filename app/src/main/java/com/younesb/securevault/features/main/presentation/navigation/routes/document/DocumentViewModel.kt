package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.usecases.GetDocumentUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.OpenDocumentUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DocumentViewModel(
    getDocumentUseCase: GetDocumentUseCase,
    openDocumentUseCase: OpenDocumentUseCase,
    documentId: String,
): ViewModel() {

}
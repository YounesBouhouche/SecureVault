package com.younesb.securevault.features.main.presentation.navigation.routes.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFolderUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FolderViewModel(
    observeDocumentsUseCase: ObserveDocumentsUseCase,
    observeFolderUseCase: ObserveFolderUseCase,
    folderId: String,
): ViewModel() {
    val documents = observeDocumentsUseCase(folderId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val folder = observeFolderUseCase(folderId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FolderDto(id = folderId)
    )
}
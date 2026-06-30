package com.younesb.securevault.features.main.presentation.navigation.routes.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    observeDocumentsUseCase: ObserveDocumentsUseCase,
    observeFoldersUseCase: ObserveFoldersUseCase,
    observeTagsUseCase: ObserveTagsUseCase
): ViewModel() {

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
    val documents = observeDocumentsUseCase("").stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val folders = observeFoldersUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun toggleTagSelection(id: String) {
        _selectedTags.update { oldSet ->
            if (id in oldSet) oldSet - id else oldSet + id
        }
    }
}
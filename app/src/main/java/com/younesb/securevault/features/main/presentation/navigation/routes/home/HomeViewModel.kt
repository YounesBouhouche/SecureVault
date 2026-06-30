package com.younesb.securevault.features.main.presentation.navigation.routes.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.main.domain.usecases.ObserveDocumentsUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveFoldersUseCase
import com.younesb.securevault.features.main.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    observeDocumentsUseCase: ObserveDocumentsUseCase,
    observeFoldersUseCase: ObserveFoldersUseCase,
    observeTagsUseCase: ObserveTagsUseCase
) : ViewModel() {

    private val _selectedTags = MutableStateFlow(emptySet<String>())
    private val _query = MutableStateFlow("")
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

    private val _documents = observeDocumentsUseCase("")
    val documents = combine(_documents, _selectedTags, _query) { documents, selectedTags, query ->
        documents.filter { document ->
            (selectedTags.isEmpty() || document.tags.any { it.id in selectedTags }) &&
                    (query.isBlank() || document.name.contains(query, ignoreCase = true))
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _folders = observeFoldersUseCase()
    val folders = combine(_folders, _query) { folders, query ->
        folders.filter { folder ->
            (query.isBlank() || folder.name.contains(query, ignoreCase = true))
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun toggleTagSelection(id: String) {
        _selectedTags.update { oldSet ->
            if (id in oldSet) oldSet - id else oldSet + id
        }
    }

    fun setQuery(query: String) {
        _query.value = query
    }
}
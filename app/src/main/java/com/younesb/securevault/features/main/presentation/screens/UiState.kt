package com.younesb.securevault.features.main.presentation.screens

import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocState
import com.younesb.securevault.features.main.presentation.components.new_doc.DocNameError

data class UiState(
    val showNewFolderDialog: Boolean = false,
    val newFolderLoading: Boolean = false,
    val showNewTagDialog: Boolean = false,
    val newTagLoading: Boolean = false,
    val newDocState: NewDocState = NewDocState.Collapsed,
    val newDocName: String? = null,
    val docNameError: DocNameError? = null,
)

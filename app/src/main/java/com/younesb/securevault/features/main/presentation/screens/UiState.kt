package com.younesb.securevault.features.main.presentation.screens

data class UiState(
    val showNewFolderDialog: Boolean = false,
    val newFolderLoading: Boolean = false,
    val showNewTagDialog: Boolean = false,
    val newTagLoading: Boolean = false,
)

package com.younesb.securevault.features.main.presentation.screens

import com.younesb.securevault.features.main.presentation.util.NewItemType

sealed interface Action {
    data class ShowFilePicker(val type: NewItemType): Action
    data object ShowNewFolderDialog: Action
    data object HideNewFolderDialog: Action
    data class CreateFolder(val name: String): Action
}
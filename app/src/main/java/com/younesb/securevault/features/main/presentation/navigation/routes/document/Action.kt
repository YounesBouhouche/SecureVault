package com.younesb.securevault.features.main.presentation.navigation.routes.document

sealed interface Action {
    data object ToggleFavorite: Action
    data object ShowRenameDialog: Action
    data object HideRenameDialog: Action
    data class Rename(val newName: String): Action
    data object ShowDeleteDialog: Action
    data object HideDeleteDialog: Action
    data object Delete: Action
    data object ExportFile: Action
}
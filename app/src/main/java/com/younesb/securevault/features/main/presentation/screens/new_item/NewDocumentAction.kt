package com.younesb.securevault.features.main.presentation.screens.new_item

sealed interface NewDocumentAction {
    data object RemoveError: NewDocumentAction
    data class SelectFolder(val index: Int?): NewDocumentAction
    data class SetTagSelected(val id: String, val selected: Boolean): NewDocumentAction
    data class Confirm(
        val name: String,
        val description: String,
        val noteContent: String = "",
    ): NewDocumentAction
    data object Dismiss: NewDocumentAction
}
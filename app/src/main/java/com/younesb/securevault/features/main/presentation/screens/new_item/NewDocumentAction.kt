package com.younesb.securevault.features.main.presentation.screens.new_item

sealed interface NewDocumentAction {
    data class SelectFolder(val index: Int?): NewDocumentAction
    data class Confirm(
        val name: String,
        val description: String,
        val noteContent: String = "",
    ): NewDocumentAction
    data object Dismiss: NewDocumentAction
}
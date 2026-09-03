package com.younesb.securevault.features.main.presentation.screens

import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocState

sealed interface Action {
    data class ShowExportSheet(val ids: List<String>? = null): Action
    data class UpdateNewDocState(val newDocState: NewDocState): Action
    data class CreateNewDoc(
        val name: String,
        val description: String
    ): Action
}
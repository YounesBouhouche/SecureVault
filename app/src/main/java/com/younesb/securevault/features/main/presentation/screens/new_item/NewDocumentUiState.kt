package com.younesb.securevault.features.main.presentation.screens.new_item

import android.net.Uri
import com.younesb.securevault.features.main.domain.models.DocumentType

data class NewDocumentUiState(
    val sheetVisible: Boolean = false,
    val fileUri: Uri? = null,
    val type: DocumentType = DocumentType.FILE,
    val name: String = "",
    val tags: List<String> = emptyList(),
)

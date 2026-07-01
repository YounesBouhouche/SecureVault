package com.younesb.securevault.features.main.presentation.screens.new_item

import android.net.Uri
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.models.TagDto

data class NewDocumentUiState(
    val sheetVisible: Boolean = false,
    val fileUri: Uri? = null,
    val type: DocumentType = DocumentType.FILE,
    val mimeType: String = "",
    val name: String = "",
    val tags: List<TagDto> = emptyList(),
    val folders: List<FolderDto> = emptyList(),
    val selectedFolder: Int? = null,
    val selectedTags: Set<String> = emptySet(),
    val docNameError: DocNameError? = null
)

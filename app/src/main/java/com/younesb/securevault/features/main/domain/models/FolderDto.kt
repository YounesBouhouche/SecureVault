package com.younesb.securevault.features.main.domain.models

import com.younesb.securevault.features.main.data.datasource.local.database.models.Folder
import com.younesb.securevault.features.main.data.datasource.local.database.models.FolderWithDocuments

data class FolderDto(
    val id: String,
    val name: String,
    val createdAt: Long,
    val documents: List<DocumentDto>
) {
    companion object {
        fun fromFolderWithDocuments(folderWithDocuments: FolderWithDocuments): FolderDto {
            return FolderDto(
                id = folderWithDocuments.folder.id,
                name = folderWithDocuments.folder.name,
                createdAt = folderWithDocuments.folder.createdAt,
                documents = folderWithDocuments.documents.map(DocumentDto::fromDocument)
            )
        }

        fun fromFolder(folder: Folder): FolderDto {
            return FolderDto(
                id = folder.id,
                name = folder.name,
                createdAt = folder.createdAt,
                documents = emptyList()
            )
        }
    }
}
package com.younesb.securevault.features.main.domain.repository

import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.models.TagDto
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    fun observeFolders(): Flow<List<FolderDto>>
    suspend fun getFolders(): List<FolderDto>
    suspend fun createFolder(name: String): FolderDto
    suspend fun deleteFolder(folderId: String, deleteDocuments: Boolean = false)
    suspend fun renameFolder(folderId: String, newName: String)

    fun observeDocuments(): Flow<List<DocumentDto>>
    suspend fun getDocuments(): List<DocumentDto>
    suspend fun createDocument(request: DocumentDto)
    suspend fun deleteDocument(documentId: String)
    suspend fun renameDocument(documentId: String, newName: String)
    suspend fun moveDocument(documentId: String, newFolderId: String?)
    suspend fun addTagToDocument(documentId: String, tagId: String)
    suspend fun removeTagFromDocument(documentId: String, tagId: String)

    fun observeTags(): Flow<List<TagDto>>
    suspend fun getTags(): List<TagDto>
    suspend fun createTag(name: String)
    suspend fun deleteTag(tagId: String)
}
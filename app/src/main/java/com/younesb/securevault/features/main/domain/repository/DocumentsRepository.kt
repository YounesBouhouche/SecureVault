package com.younesb.securevault.features.main.domain.repository

import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.models.TagDto
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    fun observeFolders(): Flow<List<FolderDto>>
    suspend fun getFolders(): Result<List<FolderDto>, Exception>
    suspend fun createFolder(name: String): Result<FolderDto, Exception>
    suspend fun deleteFolder(folderId: String, deleteDocuments: Boolean = false): EmptyResult<Exception>
    suspend fun renameFolder(folderId: String, newName: String): EmptyResult<Exception>

    fun observeDocuments(): Flow<List<DocumentDto>>
    suspend fun getDocuments(): Result<List<DocumentDto>, Exception>
    suspend fun createDocument(request: DocumentDto): EmptyResult<Exception>
    suspend fun deleteDocument(documentId: String): EmptyResult<Exception>
    suspend fun renameDocument(documentId: String, newName: String): EmptyResult<Exception>
    suspend fun moveDocument(documentId: String, newFolderId: String?): EmptyResult<Exception>
    suspend fun addTagToDocument(documentId: String, tagId: String): EmptyResult<Exception>
    suspend fun removeTagFromDocument(documentId: String, tagId: String): EmptyResult<Exception>

    fun observeTags(): Flow<List<TagDto>>
    suspend fun getTags(): Result<List<TagDto>, Exception>
    suspend fun createTag(name: String): EmptyResult<Exception>
    suspend fun deleteTag(tagId: String): EmptyResult<Exception>
}
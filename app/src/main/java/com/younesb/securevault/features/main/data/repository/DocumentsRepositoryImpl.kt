package com.younesb.securevault.features.main.data.repository

import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.data.datasource.local.database.dao.DocumentsDao
import com.younesb.securevault.features.main.data.datasource.local.database.models.Document
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentTagCrossRef
import com.younesb.securevault.features.main.data.datasource.local.database.models.Folder
import com.younesb.securevault.features.main.data.datasource.local.database.models.Tag
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.models.TagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class DocumentsRepositoryImpl(
    val dao: DocumentsDao
) : DocumentsRepository {
    override fun observeFolders(): Flow<List<FolderDto>> =
        dao.observeFoldersWithDocuments().map {
            it.map(FolderDto::fromFolderWithDocuments)
        }

    override suspend fun getFolders(): Result<List<FolderDto>, Exception> =
        Result.run {
            dao.getFoldersWithDocuments().map(FolderDto::fromFolderWithDocuments)
        }

    override suspend fun createFolder(name: String): Result<FolderDto, Exception> =
        Result.run {
            FolderDto.fromFolder(
                dao.upsertFolder(
                    Folder(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        createdAt = System.currentTimeMillis()
                    )
                )
            )
        }

    override suspend fun deleteFolder(
        folderId: String,
        deleteDocuments: Boolean
    ): EmptyResult<Exception> = Result.run {
        dao.deleteFolder(folderId)
        if (deleteDocuments) dao.deleteDocumentsByFolderId(folderId)
    }

    override suspend fun renameFolder(folderId: String, newName: String): EmptyResult<Exception> =
        Result.run {
            val folder = dao.getFolderById(folderId)?.folder
                ?: throw IllegalArgumentException("Folder with id $folderId not found")
            dao.upsertFolder(folder.copy(name = newName))
        }

    override fun observeDocuments(): Flow<List<DocumentDto>> =
        dao.observeDocumentsWithTags().map {
            it.map(DocumentDto::fromDocumentWithTags)
        }

    override suspend fun getDocuments(): Result<List<DocumentDto>, Exception> = Result.run {
        dao.getDocumentsWithTags().map(DocumentDto::fromDocumentWithTags)
    }

    override suspend fun createDocument(request: DocumentDto): EmptyResult<Exception> = Result.run {
        dao.upsertDocument(
            Document(
                id = UUID.randomUUID().toString(),
                name = request.name,
                createdAt = System.currentTimeMillis(),
                folderId = request.folderId,
                type = request.type.name,
                size = request.size,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    private suspend fun getDocumentById(documentId: String): Document {
        return dao.getDocumentsWithTags().find { it.document.id == documentId }?.document
            ?: throw IllegalArgumentException("Document with id $documentId not found")
    }

    override suspend fun deleteDocument(documentId: String): EmptyResult<Exception> = Result.run {
        dao.deleteDocument(getDocumentById(documentId))
    }

    override suspend fun renameDocument(
        documentId: String,
        newName: String
    ): EmptyResult<Exception> = Result.run {
        val document = getDocumentById(documentId)
        dao.upsertDocument(
            document.copy(
                name = newName,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun moveDocument(
        documentId: String,
        newFolderId: String?
    ): EmptyResult<Exception> = Result.run {
        val document = getDocumentById(documentId)
        dao.upsertDocument(
            document.copy(
                folderId = newFolderId,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun addTagToDocument(
        documentId: String,
        tagId: String
    ): EmptyResult<Exception> = Result.run {
        dao.insertDocumentTagCrossRef(
            DocumentTagCrossRef(
                documentId = documentId,
                tagId = tagId
            )
        )
    }

    override suspend fun removeTagFromDocument(
        documentId: String,
        tagId: String
    ): EmptyResult<Exception> = Result.run {
        dao.deleteDocumentTagCrossRef(
            DocumentTagCrossRef(
                documentId = documentId,
                tagId = tagId
            )
        )
    }

    override fun observeTags(): Flow<List<TagDto>> =
        dao.observeTags().map {
            it.map(TagDto::fromTag)
        }

    override suspend fun getTags(): Result<List<TagDto>, Exception> =
        Result.run {
            dao.getTags().map(TagDto::fromTag)
        }

    override suspend fun createTag(name: String): EmptyResult<Exception> = Result.run {
        dao.upsertTag(
            Tag(
                id = UUID.randomUUID().toString(),
                name = name,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteTag(tagId: String): EmptyResult<Exception> = Result.run {
        val tag = dao.getTagById(tagId)
            ?: throw IllegalArgumentException("Tag with id $tagId not found")
        dao.deleteTag(tag)
    }
}
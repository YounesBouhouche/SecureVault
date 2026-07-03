package com.younesb.securevault.features.main.data.repository

import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.data.datasource.local.database.DocumentsDatabase
import com.younesb.securevault.features.main.data.datasource.local.database.dao.DocumentsDao
import com.younesb.securevault.features.main.data.datasource.local.database.models.Document
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentTagCrossRef
import com.younesb.securevault.features.main.data.datasource.local.database.models.Folder
import com.younesb.securevault.features.main.data.datasource.local.database.models.Tag
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.FolderDto
import com.younesb.securevault.features.main.domain.models.TagDto
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class DocumentsRepositoryImpl(
    val db: DocumentsDatabase
) : DocumentsRepository {
    private val dao: DocumentsDao = db.dao

    override fun observeFolders(): Flow<List<FolderDto>> =
        dao.observeFoldersWithDocuments().map {
            it.map(FolderDto::fromFolderWithDocuments)
        }

    override fun observeFolder(folderId: String): Flow<FolderDto> =
        dao.observeFolderWithDocuments(folderId).map {
            FolderDto.fromFolderWithDocuments(it)
        }

    override suspend fun getFolders(): Result<List<FolderDto>, Exception> =
        Result.run {
            dao.getFoldersWithDocuments().map(FolderDto::fromFolderWithDocuments)
        }

    override suspend fun createFolder(name: String): EmptyResult<Exception> =
        Result.run {
            dao.upsertFolder(
                Folder(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    createdAt = System.currentTimeMillis()
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

    override fun observeDocuments(folderId: String): Flow<List<DocumentDto>> =
        dao.observeDocumentsWithTags(folderId).map {
            it.map(DocumentDto::fromDocumentWithTags)
        }

    override fun observeAllDocuments(): Flow<List<DocumentDto>> =
        dao.observeAllDocumentsWithTags().map {
            it.map(DocumentDto::fromDocumentWithTags)
        }

    override suspend fun getDocuments(): Result<List<DocumentDto>, Exception> = Result.run {
        dao.getDocumentsWithTags().map(DocumentDto::fromDocumentWithTags)
    }

    private suspend fun getDocument(documentId: String): Document =
        dao.getDocumentById(documentId)
            ?: throw IllegalArgumentException("Document with id $documentId not found")

    override suspend fun getDocumentById(documentId: String): Result<DocumentDto, Exception> =
        Result.run {
            DocumentDto.fromDocument(getDocument(documentId))
        }

    override suspend fun getDocumentsByIds(ids: List<String>): Result<List<DocumentDto>, Exception>  =
        Result.run {
            ids.map { documentId ->
                DocumentDto.fromDocument(getDocument(documentId))
            }
        }

    override suspend fun createDocument(document: DocumentDto): EmptyResult<Exception> =
        Result.run {
            dao.upsertDocument(
                Document(
                    id = document.id.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString(),
                    name = document.name,
                    createdAt = System.currentTimeMillis(),
                    folderId = document.folderId,
                    type = document.type.name,
                    mimeType = document.mimeType,
                    size = document.size,
                    favorite = false,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

    override suspend fun deleteDocument(documentId: String): EmptyResult<Exception> = Result.run {
        dao.deleteDocumentId(documentId)
    }


    override suspend fun renameDocument(
        documentId: String,
        newName: String
    ): EmptyResult<Exception> = Result.run {
        dao.upsertDocument(
            getDocument(documentId).copy(
                name = newName,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun setFavorite(
        documentId: String,
        favorite: Boolean
    ): EmptyResult<Exception> = Result.run {
        dao.setFavorite(documentId, favorite)
    }

    override suspend fun moveDocument(
        documentId: String,
        newFolderId: String
    ): EmptyResult<Exception> = Result.run {
        dao.upsertDocument(
            getDocument(documentId).copy(
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

    override suspend fun deleteAllDocuments(): EmptyResult<Exception> {
        return withContext(Dispatchers.IO) {
            Result.run {
                db.clearAllTables()
//                dao.clearPrimaryKeys()
            }
        }
    }

    override suspend fun checkIfDocumentExists(name: String): Result<Boolean, Exception> =
        Result.run {
            dao.checkIfDocumentExists(name)
        }
}
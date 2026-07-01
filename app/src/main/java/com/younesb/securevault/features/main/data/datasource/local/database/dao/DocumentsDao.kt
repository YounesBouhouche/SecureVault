package com.younesb.securevault.features.main.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.younesb.securevault.features.main.data.datasource.local.database.models.Document
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentTagCrossRef
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentWithTags
import com.younesb.securevault.features.main.data.datasource.local.database.models.Folder
import com.younesb.securevault.features.main.data.datasource.local.database.models.FolderWithDocuments
import com.younesb.securevault.features.main.data.datasource.local.database.models.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentsDao {
    @Transaction
    @Query("SELECT * FROM Folder")
    suspend fun getFoldersWithDocuments(): List<FolderWithDocuments>

    @Transaction
    @Query("SELECT * FROM Folder WHERE id = :folderId")
    suspend fun getFolderById(folderId: String): FolderWithDocuments?

    @Transaction
    @Query("SELECT * FROM Folder")
    fun observeFoldersWithDocuments(): Flow<List<FolderWithDocuments>>

    @Transaction
    @Query("SELECT * FROM Folder where id=:folderId")
    fun observeFolderWithDocuments(folderId: String): Flow<FolderWithDocuments>

    @Transaction
    @Query("SELECT * FROM Document")
    suspend fun getDocumentsWithTags(): List<DocumentWithTags>

    @Transaction
    @Query("SELECT * FROM Document where folderId=:folderId")
    fun observeDocumentsWithTags(folderId: String?): Flow<List<DocumentWithTags>>

    @Transaction
    @Query("SELECT * FROM Document")
    fun observeAllDocumentsWithTags(): Flow<List<DocumentWithTags>>

    @Query("SELECT * FROM Tag")
    fun observeTags(): Flow<List<Tag>>

    @Query("SELECT * FROM Tag")
    suspend fun getTags(): List<Tag>

    @Query("SELECT * FROM Tag WHERE id = :tagId")
    suspend fun getTagById(tagId: String): Tag?

    @Upsert
    suspend fun upsertDocument(document: Document)

    @Query("UPDATE Document SET favorite = :favorite WHERE id = :documentId")
    suspend fun setFavorite(documentId: String, favorite: Boolean)

    @Query("SELECT * FROM Document WHERE id = :documentId")
    suspend fun getDocumentById(documentId: String): Document?

    @Delete
    suspend fun deleteDocument(document: Document)

    @Query("DELETE FROM Document WHERE id = :documentId")
    suspend fun deleteDocumentId(documentId: String)

    @Query("DELETE FROM Document WHERE folderId = :folderId")
    suspend fun deleteDocumentsByFolderId(folderId: String)

    @Upsert
    suspend fun upsertFolder(folder: Folder)

    @Query("DELETE FROM Folder WHERE id = :folderId")
    suspend fun deleteFolder(folderId: String)

    @Upsert
    suspend fun upsertTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Insert
    suspend fun insertDocumentTagCrossRef(ref: DocumentTagCrossRef)

    @Delete
    suspend fun deleteDocumentTagCrossRef(ref: DocumentTagCrossRef)

    @Query("SELECT EXISTS(SELECT 1 FROM Document WHERE name = :name LIMIT 1)")
    suspend fun checkIfDocumentExists(name: String): Boolean

//    @Query("DELETE FROM sqlite_sequence")
//    suspend fun clearPrimaryKeys()
}
package com.younesb.securevault.features.main.data.datasource.local.database.models

import androidx.room.Embedded
import androidx.room.Relation


data class FolderWithDocuments(
    @Embedded
    val folder: Folder,
    @Relation(
        parentColumn = "id",
        entityColumn = "folderId"
    )
    val documents: List<Document>
)

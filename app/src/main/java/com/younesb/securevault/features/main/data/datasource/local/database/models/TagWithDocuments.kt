package com.younesb.securevault.features.main.data.datasource.local.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithDocuments(
    @Embedded
    val tag: Tag,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DocumentTagCrossRef::class,
            parentColumn = "tagId",
            entityColumn = "documentId"
        )
    )
    val documents: List<Document>
)

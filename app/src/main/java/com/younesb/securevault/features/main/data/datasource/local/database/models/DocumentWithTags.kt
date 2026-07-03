package com.younesb.securevault.features.main.data.datasource.local.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DocumentWithTags(
    @Embedded
    val document: Document,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = DocumentTagCrossRef::class,
            parentColumn = "documentId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>
)

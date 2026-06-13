package com.younesb.securevault.features.main.data.datasource.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Document(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val size: Long,
    val folderId: String?,
    val type: String,
    val createdAt: Long,
    val updatedAt: Long
)

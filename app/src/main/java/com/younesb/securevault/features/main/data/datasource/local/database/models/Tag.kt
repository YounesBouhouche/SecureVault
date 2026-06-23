package com.younesb.securevault.features.main.data.datasource.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val createdAt: Long
)

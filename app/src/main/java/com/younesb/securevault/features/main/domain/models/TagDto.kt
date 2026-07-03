package com.younesb.securevault.features.main.domain.models

import com.younesb.securevault.features.main.data.datasource.local.database.models.Tag

data class TagDto(
    val id: String,
    val name: String,
    val createdAt: Long
) {
    companion object {
        fun fromTag(tag: Tag): TagDto {
            return TagDto(
                id = tag.id,
                name = tag.name,
                createdAt = tag.createdAt
            )
        }
    }
}

package com.younesb.securevault.features.main.domain.models

import com.younesb.securevault.features.main.data.datasource.local.database.models.Document
import com.younesb.securevault.features.main.data.datasource.local.database.models.DocumentWithTags

data class DocumentDto(
    val id: String = "",
    val name: String = "",
    val createdAt: Long = 0L,
    val folderId: String? = null,
    val size: Long = 0L,
    val type: DocumentType = DocumentType.FILE,
    val tags: List<TagDto> = emptyList()
) {
    companion object {
        fun fromDocumentWithTags(documentWithTags: DocumentWithTags): DocumentDto {
            return DocumentDto(
                id = documentWithTags.document.id,
                name = documentWithTags.document.name,
                createdAt = documentWithTags.document.createdAt,
                folderId = documentWithTags.document.folderId,
                size = documentWithTags.document.size,
                type = try {
                    DocumentType.valueOf(documentWithTags.document.type)
                } catch (_: IllegalArgumentException) {
                    DocumentType.UNKNOWN
                },
                tags = documentWithTags.tags.map(TagDto::fromTag)
            )
        }

        fun fromDocument(document: Document): DocumentDto {
            return DocumentDto(
                id = document.id,
                name = document.name,
                createdAt = document.createdAt,
                folderId = document.folderId,
                size = document.size,
                type = try {
                    DocumentType.valueOf(document.type)
                } catch (_: IllegalArgumentException) {
                    DocumentType.UNKNOWN
                },
                tags = emptyList()
            )
        }
    }

    class Builder {
        private var id: String = ""
        private var name: String = ""
        private var createdAt: Long = 0L
        private var folderId: String? = null
        private var size: Long = 0L
        private var type: DocumentType = DocumentType.FILE
        private var tags: List<TagDto> = emptyList()

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun createdAt(createdAt: Long) = apply { this.createdAt = createdAt }
        fun folderId(folderId: String?) = apply { this.folderId = folderId }
        fun size(size: Long) = apply { this.size = size }
        fun type(type: DocumentType) = apply { this.type = type }
        fun tags(tags: List<TagDto>) = apply { this.tags = tags }

        fun build() = DocumentDto(id, name, createdAt, folderId, size, type, tags)
    }
}
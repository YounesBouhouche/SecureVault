package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import java.util.UUID

class SaveNoteUseCase(
    val filesRepository: FilesRepository,
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(
        content: String,
        document: DocumentDto
    ) =
        document.copy(id = document.id.ifBlank { UUID.randomUUID().toString() }).let { doc ->
            filesRepository.saveFile(content, doc.id)
                .pipe {
                    documentsRepository.createDocument(doc.copy(size = it))
                }
        }
}
package com.younesb.securevault.features.main.domain.usecases

import android.net.Uri
import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository
import java.util.UUID

class SaveDocumentUseCase(
    val filesRepository: FilesRepository,
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(
        sourceFile: Uri,
        document: DocumentDto
    ) =
        document.copy(id = document.id.ifBlank { UUID.randomUUID().toString() }).let { doc ->
            filesRepository.saveFile(sourceFile, doc.id)
                .pipe {
                    documentsRepository.createDocument(doc)
                }
        }
}
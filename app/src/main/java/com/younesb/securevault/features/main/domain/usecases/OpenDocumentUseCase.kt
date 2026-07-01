package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository

class OpenDocumentUseCase(
    val filesRepository: FilesRepository,
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(id: String) =
        documentsRepository.getDocumentById(id)
            .pipe { document ->
                filesRepository.getFileContent(document.id, true)
            }
}
package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository

class DeleteDocumentUseCase(
    val filesRepository: FilesRepository,
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(documentId: String) =
        filesRepository.deleteFile(documentId).pipe {
            documentsRepository.deleteDocument(documentId)
        }
}
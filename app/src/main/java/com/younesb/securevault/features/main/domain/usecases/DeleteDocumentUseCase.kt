package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class DeleteDocumentUseCase(val documentsRepository: DocumentsRepository) {
    suspend operator fun invoke(documentId: String) =
        documentsRepository.deleteDocument(documentId)
}
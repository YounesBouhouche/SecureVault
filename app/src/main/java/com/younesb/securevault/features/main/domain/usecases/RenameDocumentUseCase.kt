package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class RenameDocumentUseCase(val documentsRepository: DocumentsRepository) {
    suspend operator fun invoke(documentId: String, newName: String) =
        documentsRepository.renameDocument(documentId, newName)
}
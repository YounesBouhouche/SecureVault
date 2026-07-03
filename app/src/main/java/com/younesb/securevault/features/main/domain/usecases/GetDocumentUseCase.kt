package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class GetDocumentUseCase(val documentsRepository: DocumentsRepository) {
    suspend operator fun invoke(folderId: String) =
        documentsRepository.getDocumentById(folderId)
}
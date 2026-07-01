package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class GetDocumentsUseCase(val documentsRepository: DocumentsRepository) {
    suspend operator fun invoke(ids: List<String>? = null) =
        if (ids == null) documentsRepository.getDocuments()
        else documentsRepository.getDocumentsByIds(ids)
}
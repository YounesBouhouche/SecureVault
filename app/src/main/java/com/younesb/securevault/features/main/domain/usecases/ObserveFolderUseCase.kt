package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class ObserveFolderUseCase(val documentsRepository: DocumentsRepository) {
    operator fun invoke(folderId: String) = documentsRepository.observeFolder(folderId)
}
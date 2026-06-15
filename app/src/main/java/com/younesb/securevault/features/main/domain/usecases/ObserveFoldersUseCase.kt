package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class ObserveFoldersUseCase(
    val documentsRepository: DocumentsRepository
) {
    operator fun invoke() = documentsRepository.observeFolders()
}

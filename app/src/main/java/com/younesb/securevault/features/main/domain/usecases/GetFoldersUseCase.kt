package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class GetFoldersUseCase(
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke() = documentsRepository.getFolders()
}

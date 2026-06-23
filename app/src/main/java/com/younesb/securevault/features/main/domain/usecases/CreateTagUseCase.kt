package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class CreateTagUseCase(
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(name: String) = documentsRepository.createTag(name)
}

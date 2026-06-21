package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class ObserveTagsUseCase(
    val documentsRepository: DocumentsRepository
) {
    operator fun invoke() = documentsRepository.observeTags()
}

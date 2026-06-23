package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class SetFavoriteUseCase(
    val documentsRepository: DocumentsRepository
) {
    suspend operator fun invoke(
        id: String,
        favorite: Boolean
    ) = documentsRepository.setFavorite(id, favorite)
}
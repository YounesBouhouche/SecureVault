package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository

class CheckForFileExistence(
    val repository: DocumentsRepository
) {
    suspend operator fun invoke(name: String): Result<Boolean, Exception> = repository.checkIfDocumentExists(name)
}
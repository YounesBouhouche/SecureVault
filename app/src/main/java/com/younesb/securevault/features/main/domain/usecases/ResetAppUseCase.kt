package com.younesb.securevault.features.main.domain.usecases

import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.core.domain.utils.pipe
import com.younesb.securevault.features.main.domain.repository.DocumentsRepository
import com.younesb.securevault.features.main.domain.repository.FilesRepository

class ResetAppUseCase(
    val authRepository: AuthRepository,
    val documentsRepository: DocumentsRepository,
    val filesRepository: FilesRepository
) {
    suspend operator fun invoke(): EmptyResult<Exception> =
        filesRepository.deleteAllFiles().pipe {
            documentsRepository.deleteAllDocuments()
        }.pipe {
            Result.run {
                authRepository.resetCredentials()
            }
        }
}
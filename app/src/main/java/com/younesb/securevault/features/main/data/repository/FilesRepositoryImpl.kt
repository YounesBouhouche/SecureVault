package com.younesb.securevault.features.main.data.repository

import android.net.Uri
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.data.files_utils.FilesManager
import com.younesb.securevault.features.main.domain.repository.FilesRepository

class FilesRepositoryImpl(
    val filesManager: FilesManager
) : FilesRepository {
    override suspend fun getFileContent(name: String): Result<ByteArray, Exception> =
        Result.run {
            filesManager.readFile(filesManager.getUri(name), encrypted = true)
        }

    override suspend fun saveFile(
        sourceUri: Uri,
        destinationFileName: String
    ): EmptyResult<Exception> = Result.run {
        val content = filesManager.readFile(sourceUri)
        val destinationUri = filesManager.getUri(destinationFileName)
        filesManager.writeFile(
            destinationUri = destinationUri,
            content = content,
            encrypted = true
        )
    }

    override suspend fun saveFile(
        content: String,
        destinationFileName: String
    ): EmptyResult<Exception> = Result.run {
        val destinationUri = filesManager.getUri(destinationFileName)
        filesManager.writeFile(
            destinationUri = destinationUri,
            content = content.toByteArray(),
            encrypted = true
        )
    }
}
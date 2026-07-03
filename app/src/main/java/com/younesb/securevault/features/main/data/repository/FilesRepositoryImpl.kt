package com.younesb.securevault.features.main.data.repository

import android.net.Uri
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result
import com.younesb.securevault.features.main.data.files_utils.FilesManager
import com.younesb.securevault.features.main.domain.repository.FilesRepository

class FilesRepositoryImpl(
    val filesManager: FilesManager
) : FilesRepository {
    override suspend fun getFileContent(
        name: String,
        encrypted: Boolean
    ): Result<ByteArray, Exception> =
        Result.run {
            filesManager.readFile(filesManager.getUri(name), encrypted)
        }

    override suspend fun getFileUri(name: String, external: Boolean): Uri =
        filesManager.getUri(name, external)

    override suspend fun saveFile(
        sourceUri: Uri,
        destinationFileName: String
    ): Result<Long, Exception> = Result.run {
        val content = filesManager.readFile(sourceUri)
        val destinationUri = filesManager.getUri(destinationFileName)
        filesManager.writeFile(
            destinationUri = destinationUri,
            content = content,
            encrypted = true
        )
        content.size.toLong()
    }

    override suspend fun saveFile(
        content: String,
        destinationFileName: String
    ): Result<Long, Exception> = Result.run {
        val destinationUri = filesManager.getUri(destinationFileName)
        content.toByteArray().also {
            filesManager.writeFile(
                destinationUri = destinationUri,
                content = it,
                encrypted = true
            )
        }.size.toLong()
    }

    override suspend fun deleteFile(name: String, external: Boolean): EmptyResult<Exception> =
        Result.run {
            filesManager.deleteFile(filesManager.getUri(name, external))
        }

    override suspend fun deleteFileFromUri(uri: Uri): Result<Boolean, Exception> =
        Result.run {
            filesManager.deleteFileFromContent(uri)
        }

    override suspend fun deleteAllFiles(): Result<Boolean, Exception> {
        try {
            filesManager.deleteAllFiles()
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}
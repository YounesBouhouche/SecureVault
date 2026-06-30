package com.younesb.securevault.features.main.domain.repository

import android.net.Uri
import com.younesb.securevault.core.domain.utils.Result

interface FilesRepository {
    suspend fun getFileContent(name: String): Result<ByteArray, Exception>

    suspend fun saveFile(sourceUri: Uri, destinationFileName: String): Result<Long, Exception>

    suspend fun saveFile(content: String, destinationFileName: String): Result<Long, Exception>
    suspend fun deleteFile(name: String): Result<Boolean, Exception>
    suspend fun deleteAllFiles(): Result<Boolean, Exception>
}
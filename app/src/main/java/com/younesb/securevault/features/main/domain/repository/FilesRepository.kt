package com.younesb.securevault.features.main.domain.repository

import android.net.Uri
import com.younesb.securevault.core.domain.utils.EmptyResult
import com.younesb.securevault.core.domain.utils.Result

interface FilesRepository {
    suspend fun getFileContent(name: String): Result<ByteArray, Exception>

    suspend fun saveFile(sourceUri: Uri, destinationFileName: String): EmptyResult<Exception>

    suspend fun saveFile(content: String, destinationFileName: String): EmptyResult<Exception>
}
package com.younesb.securevault.features.main.data.files_utils

import android.content.Context
import android.net.Uri
import com.younesb.securevault.core.util.crypto.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

class FilesManager(
    val context: Context,
    val crypto: Crypto
) {
    private inline fun <T> Uri.safeCall(createIfNotExist: Boolean = false, block: (Uri) -> T): T =
        try {
            block(this)
        } catch (e: FileNotFoundException) {
            if (createIfNotExist) {
                context.contentResolver.openOutputStream(this)?.use { outputStream ->
                    outputStream.write(ByteArray(0))
                }
                block(this)
            } else {
                throw e
            }
        }

    suspend fun readFile(
        uri: Uri,
        encrypted: Boolean = false
    ): ByteArray {
        val content = withContext(Dispatchers.IO) {
            uri.safeCall {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: throw FileNotFoundException("Unable to open input stream for URI: $uri")
            }
        }
        return withContext(Dispatchers.Default) {
            if (encrypted) crypto.decrypt(content) else content
        }
    }

    suspend fun writeFile(
        destinationUri: Uri,
        content: ByteArray,
        encrypted: Boolean = false
    ) {
        destinationUri.safeCall {
            val dataToWrite = withContext(Dispatchers.Default) {
                if (encrypted) crypto.encrypt(content) else content
            }
            withContext(Dispatchers.IO) {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(dataToWrite)
                } ?: throw FileNotFoundException("Unable to open output stream for URI: $it")
            }
        }
    }

    fun getUri(fileName: String, external: Boolean = false): Uri {
        val dir = if (external) context.getExternalFilesDir(null) else context.filesDir
        return Uri.fromFile(File(dir, fileName))
    }

    fun deleteFile(uri: Uri) {
        uri.safeCall {
            context.contentResolver.delete(it, null, null)
        }
    }

    suspend fun deleteAllFiles(external: Boolean = false) {
        val dir = if (external) context.getExternalFilesDir(null) else context.filesDir
        withContext(Dispatchers.IO) {
            dir?.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}
package com.younesb.securevault.features.main.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap


data class FileInfo(
    val name: String?,
    val size: Long?,
    val mimeType: String?
) {
    companion object {
        fun getFileProviderInfo(context: Context, uri: Uri): FileInfo {
            with(context) {
                var name: String? = null
                var size: Long? = null

                val mimeType: String? = contentResolver.getType(uri)

                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                        if (nameIndex != -1) name = cursor.getString(nameIndex)
                        if (sizeIndex != -1) size = cursor.getLong(sizeIndex)
                    }
                }

                return FileInfo(name, size, mimeType)
            }
        }

        fun mimeTypeToExtension(mimeType: String): String? {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }
    }
}


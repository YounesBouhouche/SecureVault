package com.younesb.securevault.features.main.data.files_utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Archiver(
    val context: Context,
    val filesManager: FilesManager
) {
    suspend fun zip(files: List<Pair<Uri, String>>, zipFile: Uri, encrypted: Boolean) {
        withContext(Dispatchers.IO) {
            ZipOutputStream(context.contentResolver.openOutputStream(zipFile)).use { zipOut ->
                for ((file, name) in files) {
                    val zipEntry = ZipEntry(name)
                    zipOut.putNextEntry(zipEntry)
                    val bytes = filesManager.readFile(file, !encrypted)
                    zipOut.write(bytes)
                    zipOut.closeEntry()
                }
            }
        }
    }
}
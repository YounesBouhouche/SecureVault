package com.younesb.securevault.features.main.presentation.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.younesb.securevault.R
import java.io.File

object ComposeFileProvider: FileProvider(R.xml.file_paths) {
    fun getImageUri(context: Context): Uri {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()
        val file = File.createTempFile(
            "selected_image_",
            ".jpg",
            directory
        )
        val authority = context.packageName + ".fileprovider"
        return getUriForFile(
            context,
            authority,
            file,
        )
    }
}
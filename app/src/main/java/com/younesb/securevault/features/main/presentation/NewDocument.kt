package com.younesb.securevault.features.main.presentation

import android.net.Uri
import com.younesb.securevault.features.main.presentation.util.FileInfo

sealed class NewDocument(open val info: FileInfo? = null) {
    data class Image(val uri: Uri, override val info: FileInfo): NewDocument(info)
    data class File(val uri: Uri, override val info: FileInfo): NewDocument(info)
    data class Note(val content: String = ""): NewDocument()
}
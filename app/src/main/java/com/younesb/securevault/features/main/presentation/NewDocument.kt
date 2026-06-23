package com.younesb.securevault.features.main.presentation

import android.net.Uri

sealed interface NewDocument {
    data class Image(val uri: Uri): NewDocument
    data class File(val uri: Uri): NewDocument
    data class Note(val content: String = ""): NewDocument
}
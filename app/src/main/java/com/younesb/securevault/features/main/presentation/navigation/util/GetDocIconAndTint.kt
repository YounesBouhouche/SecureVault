package com.younesb.securevault.features.main.presentation.navigation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType

fun DocumentDto.getIconAndColor(): Pair<ImageVector, Color> {
    return when (type) {
        DocumentType.NOTE -> Pair(Icons.AutoMirrored.Rounded.Note, Color(0xFFFFC107))
        DocumentType.IMAGE -> Pair(Icons.Rounded.Image, Color(0xFF8BC34A))
        else -> when (mimeType.substringBefore("/")) {
            "video" -> Pair(Icons.Rounded.Movie, Color(0xFFFF5722))
            "audio" -> Pair(Icons.Rounded.MusicNote, Color(0xFF9C27B0))
            "text" -> Pair(Icons.AutoMirrored.Rounded.Note, Color(0xFF2196F3))
            "application" -> when (mimeType.substringAfter("/").drop(0)) {
                "pdf" -> Pair(Icons.Rounded.PictureAsPdf, Color(0xFFE91E63))
                "vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "vnd.ms-excel.sheet.macroEnabled.12",
                "vnd.ms-excel.addin.macroEnabled.12",
                "vnd.ms-excel" -> Pair(Icons.AutoMirrored.Filled.InsertDriveFile, Color(0xFF4CAF50))

                else -> Pair(Icons.AutoMirrored.Filled.InsertDriveFile, Color(0xFF607D8B))
            }

            else -> Pair(Icons.AutoMirrored.Filled.InsertDriveFile, Color(0xFF607D8B))
        }
    }
}
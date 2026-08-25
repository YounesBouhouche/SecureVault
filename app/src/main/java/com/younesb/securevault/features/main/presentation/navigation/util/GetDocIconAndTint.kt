package com.younesb.securevault.features.main.presentation.navigation.util

import androidx.compose.ui.graphics.Color
import com.younesb.securevault.R
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType

fun DocumentDto.getIconAndColor(): Pair<Int, Color> {
    return when (type) {
        DocumentType.NOTE -> Pair(R.drawable.note_stack_filled, Color(0xFFFFC107))
        DocumentType.IMAGE -> Pair(R.drawable.photo_prints_filled, Color(0xFF8BC34A))
        else -> when (mimeType.substringBefore("/")) {
            "video" -> Pair(R.drawable.videocam_filled, Color(0xFFFF5722))
            "audio" -> Pair(R.drawable.audio_file_filled, Color(0xFF9C27B0))
            "text" -> Pair(R.drawable.format_align_left_filled, Color(0xFF2196F3))
            "application" -> when (mimeType.substringAfter("/").drop(0)) {
                "pdf" -> Pair(R.drawable.book_filled, Color(0xFFE91E63))
                "vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "vnd.ms-excel.sheet.macroEnabled.12",
                "vnd.ms-excel.addin.macroEnabled.12",
                "vnd.ms-excel" -> Pair(R.drawable.table_filled, Color(0xFF4CAF50))

                else -> Pair(R.drawable.file_filled, Color(0xFF607D8B))
            }

            else -> Pair(R.drawable.file_filled, Color(0xFF607D8B))
        }
    }
}
package com.younesb.securevault.features.main.presentation.util

import androidx.compose.ui.graphics.Color
import com.younesb.securevault.R

enum class NewItemType(
    val icon: Int,
    val textRes: Int,
    val color: Color
) {
    CAMERA(R.drawable.photo_camera_filled, R.string.camera, Color(0xFF4CAF50)),
    GALLERY(R.drawable.photo_prints_filled, R.string.gallery, Color(0xFF2196F3)),
    RECORDING(R.drawable.recording_filled, R.string.recording, Color(0xFFF44336)),
    FILE(R.drawable.file_filled, R.string.file, Color(0xFFF7BB00)),
    NOTE(R.drawable.note_stack_filled, R.string.note, Color(0xFFAD00CE)),
    FOLDER(R.drawable.folder_open_filled, R.string.folder, Color(0xFF00C3A8)),
}
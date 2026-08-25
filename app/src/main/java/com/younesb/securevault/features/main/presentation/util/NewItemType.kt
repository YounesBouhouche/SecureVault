package com.younesb.securevault.features.main.presentation.util

import com.younesb.securevault.R

enum class NewItemType(
    val icon: Int,
    val textRes: Int
) {
    FILE(R.drawable.file_filled, R.string.file),
    CAMERA(R.drawable.photo_camera_filled, R.string.camera),
    GALLERY(R.drawable.photo_prints_filled, R.string.gallery),
    NOTE(R.drawable.note_stack_filled, R.string.note),
    FOLDER(R.drawable.folder_open_filled, R.string.folder)
}
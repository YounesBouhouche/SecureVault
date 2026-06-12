package com.younesb.securevault.features.main.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Download
import androidx.compose.ui.graphics.vector.ImageVector
import com.younesb.securevault.R

enum class NewItemAction(
    val icon: ImageVector,
    val textRes: Int
) {
    IMPORT(Icons.Rounded.Download, R.string._import),
    CAMERA(Icons.Rounded.CameraAlt, R.string.camera),
    NOTE(Icons.AutoMirrored.Rounded.Note, R.string.note)
}
package com.younesb.securevault.features.main.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import com.younesb.securevault.R

@Composable
fun Long.formatFileSize(): String {
    val kiloBytes = this / 1024.0
    val megaBytes = kiloBytes / 1024.0
    val gigaBytes = megaBytes / 1024.0

    val stringRes = when {
        gigaBytes >= 1 -> R.plurals.gigaBytes
        megaBytes >= 1 -> R.plurals.megaBytes
        kiloBytes >= 1 -> R.plurals.kiloBytes
        else -> R.plurals.bytes
    }

    val count = when {
        gigaBytes >= 1 -> gigaBytes
        megaBytes >= 1 -> megaBytes
        kiloBytes >= 1 -> kiloBytes
        else -> this.toDouble()
    }

    return pluralStringResource(id = stringRes, count = count.toInt(),
        formatArgs = arrayOf(count.toInt())
    )
}
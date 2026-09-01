package com.younesb.securevault.core.presentation.utils

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun expressiveGridItemShape(
    col: Int,
    colsCount: Int,
    row: Int,
    rowsCount: Int,
    largeShape: CornerBasedShape = MaterialTheme.shapes.large,
    smallShape: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    selected: Boolean = false,
): CornerBasedShape {
    val density = LocalDensity.current
    if (selected) {
        return RoundedCornerShape(100)
    }
    val lastColumn = (colsCount - 1).coerceAtLeast(0)
    val lastRow = (rowsCount - 1).coerceAtLeast(0)

    val topStart = when {
        row == 0 && col == 0 -> largeShape.topStart
        else -> smallShape.topStart
    }
    val topEnd = when {
        row == 0 && col == lastColumn -> largeShape.topEnd
        else -> smallShape.topEnd
    }
    val bottomStart = when {
        row == lastRow && col == 0 -> largeShape.bottomStart
        else -> smallShape.bottomStart
    }
    val bottomEnd = when {
        row == lastRow && col == lastColumn -> largeShape.bottomEnd
        else -> smallShape.bottomEnd
    }
    return RoundedCornerShape(
        topStart = topStart.animated(density),
        topEnd = topEnd.animated(density),
        bottomStart = bottomStart.animated(density),
        bottomEnd = bottomEnd.animated(density),
    )
}
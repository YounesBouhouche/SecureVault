package com.younesb.securevault.core.presentation.utils

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun expressiveListItemShape(
    index: Int,
    count: Int,
    largeShape: CornerBasedShape = MaterialTheme.shapes.large,
    smallShape: CornerBasedShape = MaterialTheme.shapes.extraSmall,
): CornerBasedShape {
    val topStart = when {
        index == 0 -> largeShape.topStart
        else -> smallShape.topStart
    }
    val topEnd = when {
        index == 0 -> largeShape.topEnd
        else -> smallShape.topEnd
    }
    val bottomStart = when {
        index >= count - 1 -> largeShape.bottomStart
        else -> smallShape.bottomStart
    }
    val bottomEnd = when {
        index >= count - 1 -> largeShape.bottomEnd
        else -> smallShape.bottomEnd
    }
    return RoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomStart = bottomStart,
        bottomEnd = bottomEnd,
    )
}
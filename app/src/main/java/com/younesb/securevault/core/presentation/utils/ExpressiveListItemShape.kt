package com.younesb.securevault.core.presentation.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun expressiveListItemShape(
    index: Int,
    count: Int,
    largeShape: CornerBasedShape = MaterialTheme.shapes.large,
    smallShape: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    selected: Boolean = false,
): CornerBasedShape {
    val density = LocalDensity.current
    if (selected) {
        return RoundedCornerShape(100)
    }
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
        topStart = topStart.animated(density),
        topEnd = topEnd.animated(density),
        bottomStart = bottomStart.animated(density),
        bottomEnd = bottomEnd.animated(density),
    )
}

@Composable
internal fun CornerSize.animated(density: Density) = animateFloatAsState(
    toPx(Size.Unspecified, density),
    MaterialTheme.motionScheme.fastSpatialSpec()
).value
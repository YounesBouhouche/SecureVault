package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWaveBar(
    targetHeight: Float,
    barWidth: Dp,
    maxHeight: Dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val animationScale = remember { Animatable(0f) }
    val animationSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()

    LaunchedEffect(targetHeight) {
        animationScale.animateTo(
            targetValue = 1f,
            animationSpec = animationSpec
        )
    }

    Canvas(
        modifier = Modifier
            .width(barWidth)
            .height(maxHeight)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerY = canvasHeight / 2f

        val currentBarHeight = targetHeight * animationScale.value
        val halfHeight = currentBarHeight / 2f

        val yStart = centerY - halfHeight
        val yEnd = centerY + halfHeight

        drawLine(
            color = color,
            start = Offset(canvasWidth / 2f, yStart),
            end = Offset(canvasWidth / 2f, yEnd),
            strokeWidth = canvasWidth,
            cap = StrokeCap.Round
        )
    }
}

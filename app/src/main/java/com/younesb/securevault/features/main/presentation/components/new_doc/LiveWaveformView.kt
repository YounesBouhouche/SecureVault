package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LiveWaveform(
    amplitudes: ImmutableList<Int>,
    modifier: Modifier = Modifier,
    maxHeight: Dp = 100.dp,
    barWidth: Dp = 2.dp,
    gap: Dp = 4.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        val maxBoxHeight = constraints.maxHeight
        val maxBars = (maxWidth / (barWidth + gap)).toInt()
        val visibleAmplitudes = amplitudes.takeLast(maxBars)

        Row(
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            visibleAmplitudes.forEach { amplitude ->
                val maxHeightPx = maxBoxHeight.toFloat()
                val normalizedHeight = (amplitude / 32767f) * maxHeightPx
                val absoluteHeight = normalizedHeight.coerceAtLeast(15f)

                AnimatedWaveBar(
                    targetHeight = absoluteHeight,
                    barWidth = barWidth,
                    maxHeight = maxHeight,
                    color = color
                )
            }
        }
    }
}

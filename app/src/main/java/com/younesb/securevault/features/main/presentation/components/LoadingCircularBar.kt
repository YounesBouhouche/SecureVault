package com.younesb.securevault.features.main.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun LoadingCircularBar(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularWavyProgressIndicator(
            Modifier
                .fillMaxWidth(.5f)
                .aspectRatio(1f),
            stroke =
                Stroke(
                    width = with(LocalDensity.current) { 8.dp.toPx() },
                    cap = StrokeCap.Round,
                ),
            trackStroke =
                Stroke(
                    width = with(LocalDensity.current) { 6.dp.toPx() },
                    cap = StrokeCap.Round,
                ),
            wavelength = 40.dp,
            gapSize = 8.dp,
        )
    }
}
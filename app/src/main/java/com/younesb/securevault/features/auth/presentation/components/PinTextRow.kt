package com.younesb.securevault.features.auth.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun PinTextRow(
    pin: String,
    modifier: Modifier = Modifier,
    loadingAnimation: Boolean = false,
) {
    val alpha by remember(loadingAnimation) {
        derivedStateOf {
            if (loadingAnimation) 0.5f else 1f
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.extraLargeIncreased,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).alpha(alpha),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) { index ->
                val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")
                val animatedOffset by infiniteTransition.animateFloat(
                    initialValue = -1f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000),
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(
                            offsetMillis = 100 * index,
                            offsetType = StartOffsetType.Delay
                        )
                    ),
                    label = "AlphaAnimation"
                )
                val offset = if (loadingAnimation) animatedOffset else 0f
                Box(
                    modifier = Modifier.height(70.dp)
                        .weight(1f)
                        .clip(MaterialTheme.shapes.extraExtraLarge)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        pin.getOrNull(index)?.toString(),
                        transitionSpec = {
                            (slideInVertically { it } togetherWith slideOutVertically { it }).using(
                                SizeTransform(clip = false)
                            )
                        },
                        contentAlignment = Alignment.Center
                    ) { digit ->
                        if (digit != null)
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = digit,
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.offset {
                                        IntOffset(
                                            x = 0,
                                            y = (offset * 10).dp.toPx().toInt()
                                        )
                                    }
                                )
                            }
                    }
                }
            }
        }
    }
}
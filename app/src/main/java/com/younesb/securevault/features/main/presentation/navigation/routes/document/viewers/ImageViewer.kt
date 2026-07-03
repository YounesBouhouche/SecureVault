package com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import com.younesb.securevault.core.presentation.components.Image

@Composable
fun ImageViewer(
    model: Any?,
    modifier: Modifier = Modifier,
    onToggleToolbar: () -> Unit,
    onShowInfo: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val minScale = remember { 1f }
    val maxScale = remember { 4f }
    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    val slowMovement = 0.5f
    Image(
        model = model,
        icon = Icons.Default.Image,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onShowInfo()
                    },
                    onPress = {
                        onToggleToolbar()
                    },
                    onDoubleTap = {
                        if (scale != 1f) {
                            scale = 1f
                            offsetX = initialOffset.x
                            offsetY = initialOffset.y
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Update scale with the zoom
                    val newScale = scale * zoom
                    scale = newScale.coerceIn(minScale, maxScale)

                    // Calculate new offsets based on zoom and pan
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                    val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                    // Calculate min and max offsets
                    val maxOffsetX = (size.width / 2) * (scale - 1)
                    val minOffsetX = -maxOffsetX
                    val maxOffsetY = (size.height / 2) * (scale - 1)
                    val minOffsetY = -maxOffsetY

                    // Update offsets while ensuring they stay within bounds
                    if (scale * zoom <= maxScale) {
                        offsetX = (offsetX + pan.x * scale * slowMovement + offsetXChange)
                            .coerceIn(minOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y * scale * slowMovement + offsetYChange)
                            .coerceIn(minOffsetY, maxOffsetY)
                    }

                    // Store initial offset on pan
                    if (pan != Offset(0f, 0f) && initialOffset == Offset(0f, 0f)) {
                        initialOffset = Offset(offsetX, offsetY)
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            },
        shape = RectangleShape,
        background = Color.Transparent,
        contentScale = ContentScale.Fit
    )
}
package com.younesb.securevault.features.auth.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import soup.compose.material.motion.animation.materialSharedAxisZ

enum class DragValue { Start, End }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DraggableButton(
    text: String,
    releaseText: String,
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<DragValue> = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
        )
    },
    shape: Shape = RoundedCornerShape(100),
    icon: ImageVector = Icons.AutoMirrored.Default.ArrowForward,
    color: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onSubmit: () -> Unit
) {
    val view = LocalView.current
    var viewWidth by remember {
        mutableFloatStateOf(view.width.toFloat())
    }
    val density = LocalDensity.current
    val anchors = with(density) {
        DraggableAnchors {
            DragValue.Start at 120.dp.toPx()
            DragValue.End at viewWidth
        }
    }
    val widthDp by remember {
        with(density) {
            derivedStateOf {
                state.offset.toDp()
            }
        }
    }
    val textWidth by remember {
        with(density) {
            derivedStateOf {
                state.offset.let {
                    (viewWidth - if (it.isNaN()) 120.dp.toPx() else state.offset).toDp()
                }
            }
        }
    }
    val alpha by remember {
        derivedStateOf {
            state.offset.let {
                1f - (if (it.isNaN()) 0f else state.progress(DragValue.Start, DragValue.End))
            }
        }
    }
    val animation = rememberInfiniteTransition()
    val animatedWidth by animation.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Restart,
        ),
    )
    val interactionSource = remember { MutableInteractionSource() }
    val dragging by interactionSource.collectIsDraggedAsState()
    val showReleaseText by remember {
        derivedStateOf {
            (state.currentValue == DragValue.End) and (state.settledValue == DragValue.Start) and (dragging)
        }
    }
    LaunchedEffect(viewWidth) {
        state.updateAnchors(anchors)
    }
    LaunchedEffect(state.settledValue) {
        if (state.settledValue == DragValue.End)
            onSubmit()
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .onGloballyPositioned {
                viewWidth = it.size.width.toFloat()
            }
            .onSizeChanged {
                viewWidth = it.width.toFloat()
            },
        shape = shape,
        color = containerColor,
        contentColor = color,
    ) {
        Box(Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(animatedWidth)
                    .fillMaxHeight()
                    .alpha(1f - animatedWidth),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = shape,
            ) {}
            Text(
                text = text,
                modifier = Modifier
                    .alpha(alpha)
                    .width(textWidth)
                    .align(Alignment.CenterEnd),
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            Surface(
                modifier = Modifier
                    .width(widthDp)
                    .fillMaxHeight()
                    .anchoredDraggable(
                        state = state,
                        reverseDirection = false,
                        orientation = Orientation.Horizontal,
                        interactionSource = interactionSource
                    ),
                color = color,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = shape,
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedContent(
                        targetState = showReleaseText,
                        transitionSpec = {
                            materialSharedAxisZ(targetState).using(SizeTransform(clip = false))
                        }
                    ) {
                        if (it)
                            Text(
                                text = releaseText
                            )
                        else
                            Icon(
                                icon,
                                null,
                                Modifier.size(30.dp),
                                tint = containerColor
                            )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DraggableButtonPreview() {
    Surface(
        modifier = Modifier.padding(8.dp),
    ) {
        DraggableButton(
            text = "Get started",
            releaseText = "Release to start",
        ) {
            Log.i("Onboarding", "Submit")
        }
    }
}
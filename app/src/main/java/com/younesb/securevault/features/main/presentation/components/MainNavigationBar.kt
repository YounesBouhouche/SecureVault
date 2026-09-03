package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocFab
import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocState
import com.younesb.securevault.features.main.presentation.navigation.MainNavRoutes
import com.younesb.securevault.features.main.presentation.navigation.MainRoutes

@Composable
fun MainNavigationBar(
    newDocState: NewDocState,
    onStateChange: (NewDocState) -> Unit,
    onConfirm: (name: String, description: String) -> Unit,
    route: MainNavRoutes?,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    navigate: (MainRoutes) -> Unit = { },
) {
    val expanded = newDocState != NewDocState.Collapsed
    val fabVisible = visible and (route == MainNavRoutes.HOME)
    val density = LocalDensity.current
    var navbarWidth by remember {
        mutableStateOf(0.dp)
    }
    val itemSize = navbarWidth / MainNavRoutes.entries.size
    val offset = itemSize * (route?.ordinal ?: 0)
    val animatedOffset by animateDpAsState(
        targetValue = offset,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
    )

    Box(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NewDocFab(
            modifier = Modifier.align(Alignment.BottomEnd).navigationBarsPadding(),
            state = newDocState,
            visible = fabVisible,
            onStateChange = onStateChange,
            onConfirm = onConfirm
        )
        AnimatedVisibility(
            visible = visible && !expanded,
            enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
            exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(100)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(PaddingValues(4.dp))
                        .onGloballyPositioned {
                            with(density) {
                                navbarWidth = it.size.width.toDp()
                            }
                        }
                        .onSizeChanged {
                            with(density) {
                                navbarWidth = it.width.toDp()
                            }
                        }
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .width(itemSize)
                            .fillMaxHeight()
                            .offset {
                                IntOffset(x = animatedOffset.roundToPx(), y = 0)
                            },
                        content = {},
                        shape = RoundedCornerShape(100)
                    )
                    Row(Modifier.fillMaxWidth()) {
                        MainNavRoutes.entries.forEach {
                            val selected = route == it
                            val color by animateColorAsState(
                                if (selected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                color = Color.Transparent,
                                contentColor = color,
                                shape = MaterialTheme.shapes.extraLarge,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navigate(it.destination)
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(it.icon),
                                        null,
                                        Modifier.size(24.dp)
                                    )
                                    Text(
                                        stringResource(it.label).uppercase(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 4.dp),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        softWrap = false,
                                        autoSize = TextAutoSize.StepBased(
                                            maxFontSize = MaterialTheme.typography.labelLarge.fontSize
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


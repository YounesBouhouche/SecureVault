package com.younesb.securevault.features.main.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scrim
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.utils.expressiveGridItemShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGridApi::class)
@Composable
fun <T> FabMenu(
    actions: List<T>,
    icon: @Composable (T) -> ImageVector,
    text: @Composable (T) -> String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onExpandedChange: (Boolean) -> Unit,
    onActionClick: (T) -> Unit,
) {
    val animationSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()
    if (expanded) {
        Scrim(null, Modifier.fillMaxSize(), onClick = { onExpandedChange(false) })
    }
    SharedTransitionLayout(modifier) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec) togetherWith fadeOut(animationSpec)
            }
        ) {
            if (it) {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-24).dp)
                        .sharedBounds(
                            rememberSharedContentState(key = "fab"),
                            animatedVisibilityScope = this,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            enter = fadeIn(animationSpec),
                            exit = fadeOut(animationSpec)
                        ),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TitleText(
                            text = stringResource(id = R.string.new_item),
                            modifier = Modifier.padding(16.dp),
                        )
                        Grid(
                            modifier = Modifier
                                .fillMaxWidth(),
                            config = {
                                repeat(3) {
                                    column(1/3f)
                                }
                                gap(2.dp)
                            }
                        ) {
                            actions.forEachIndexed { index, action ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    shape = expressiveGridItemShape(
                                        col = index % 3,
                                        row = index / 3,
                                        colsCount = 3,
                                        rowsCount = (actions.size + 2) / 3
                                    ),
                                    onClick = {
                                        onActionClick(action)
                                    },
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp, 16.dp)
                                    ) {
                                        Icon(
                                            imageVector = icon(action),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = text(action),
                                            softWrap = false,
                                            autoSize = TextAutoSize.StepBased(
                                                maxFontSize = MaterialTheme.typography.bodyLarge.fontSize
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                MediumFloatingActionButton(
                    onClick = {
                        onExpandedChange(!expanded)
                    },
                    modifier =
                        Modifier
                            .offset(x = (-16).dp, y = (-86).dp)
                            .sharedBounds(
                                rememberSharedContentState(key = "fab"),
                                animatedVisibilityScope = this,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                enter = fadeIn(animationSpec),
                                exit = fadeOut(animationSpec)
                            ),
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
    BackHandler(expanded) {
        onExpandedChange(false)
    }
}
package com.younesb.securevault.features.main.presentation.components.new_doc

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.Scrim
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.younesb.securevault.features.main.presentation.components.new_doc.NewDocState.*
import com.younesb.securevault.features.main.presentation.util.NewItemType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGridApi::class)
@Composable
fun NewDocFab(
    state: NewDocState,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onStateChange: (NewDocState) -> Unit,
    onConfirm: (name: String, description: String) -> Unit,
) {
    val animationSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()
    val expanded = state != Collapsed
    if (expanded) {
        Scrim(
            null,
            Modifier.fillMaxSize(),
            onClick = { onStateChange(Collapsed) }
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(
            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
            expandFrom = Alignment.BottomEnd
        ),
        exit = shrinkOut(
            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
            shrinkTowards = Alignment.BottomEnd
        ),
        modifier = modifier
    ) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec) togetherWith fadeOut(animationSpec)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) { docState ->
                when(docState) {
                    Expanded -> {
                        NewItemSheet(
                            modifier = Modifier.sheet(animationSpec),
                        ) {
                            onStateChange(New(it))
                        }
                    }
                    is New -> when(docState.type) {
                        NewItemType.CAMERA -> {
                            CameraSheet(
                                modifier = Modifier.sheet(animationSpec),
                                onClose = {
                                    onStateChange(Expanded)
                                }
                            ) {
                                onStateChange(Photo(it, it.lastPathSegment ?: ""))
                            }
                        }
                        NewItemType.RECORDING -> {
                            RecordingSheet(
                                modifier = Modifier.sheet(animationSpec),
                                onClose = {
                                    onStateChange(Expanded)
                                }
                            ) {
                                onStateChange(File(it, it.lastPathSegment ?: ""))
                            }
                        }
                        else -> {}
                    }
                    is File -> {
                        FileSheet(
                            filename = docState.name,
                            modifier = Modifier.sheet(animationSpec),
                            onCancel = {
                                onStateChange(Expanded)
                            },
                            onConfirm = { name, description ->
                                onConfirm(name, description)
                            }
                        )
                    }
                    Note -> {
                        NoteSheet(
                            modifier = Modifier.sheet(animationSpec),
                            onCancel = {
                                onStateChange(Expanded)
                            },
                            onConfirm = { name, description ->
                                onConfirm(name, description)
                            }
                        )
                    }
                    is Photo -> {
                        PhotoSheet(
                            modifier = Modifier.sheet(animationSpec),
                            uri = docState.uri.toString(),
                            filename = docState.name,
                            onCancel = {
                                onStateChange(Expanded)
                            },
                            onConfirm = { name, description ->
                                onConfirm(name, description)
                            }
                        )
                    }
                    Folder -> {
                        FolderSheet(
                            modifier = Modifier.sheet(animationSpec),
                            onCancel = {
                                onStateChange(Expanded)
                            },
                            onConfirm = {
                                onConfirm(it, "")
                            }
                        )
                    }
                    Collapsed -> {
                        MediumFloatingActionButton(
                            onClick = { onStateChange(Expanded) },
                            modifier =
                                Modifier
                                    .offset(y = (-86).dp)
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
        }
    }
    BackHandler(expanded) {
        onStateChange(Collapsed)
    }
}

@Composable
context(sharedTransitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope)
internal fun Modifier.sheet(
    animationSpec: FiniteAnimationSpec<Float> = MaterialTheme.motionScheme.fastSpatialSpec()
) = with(sharedTransitionScope) {
    then(
        Modifier.fillMaxWidth()
            .offset(y = (-24).dp)
            .sharedBounds(
                rememberSharedContentState(key = "fab"),
                animatedVisibilityScope = animatedVisibilityScope,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                enter = fadeIn(animationSpec),
                exit = fadeOut(animationSpec)
            )
    )
}

package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.models.TagDto
import com.younesb.securevault.features.main.presentation.components.TitleText
import com.younesb.securevault.features.main.presentation.navigation.routes.document.viewers.ImageViewer
import com.younesb.securevault.features.main.presentation.util.Resource
import com.younesb.securevault.features.main.presentation.util.formatFileSize
import com.younesb.securevault.features.main.presentation.util.getOrNull
import com.younesb.securevault.features.main.presentation.util.toReadableDateString


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DocumentSuccessScreen(
    document: DocumentDto,
    file: Resource<Any, Throwable>,
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(true) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(file) {
        if (document.type == DocumentType.NOTE) {
            textFieldState.setTextAndPlaceCursorAtEnd((file.getOrNull() as? String) ?: "")
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars),
        topBar = {
            AnimatedVisibility(
                uiState.controlsVisible,
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it }
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = document.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = stringResource(
                                    R.string.created,
                                    document.createdAt.toReadableDateString()
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(.5f),
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(.5f),
                    ),
                    expandedHeight = 80.dp,
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            shapes = IconButtonDefaults.shapes(),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Localized description",
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onAction(Action.ShowInfoSheet)
                            },
                            shapes = IconButtonDefaults.shapes(),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Localized description",
                            )
                        }
                    }
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                uiState.controlsVisible,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -ScreenOffset)
                        .zIndex(1f)
            ) {
                HorizontalFloatingToolbar(
                    expanded = expanded,
                    colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
                    floatingActionButton = {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = {
                                PlainTooltip(
                                    modifier =
                                        Modifier.semantics {
                                            liveRegion = LiveRegionMode.Assertive
                                            paneTitle = "Localized description"
                                        }
                                ) {
                                    Text("Localized description")
                                }
                            },
                            state = rememberTooltipState(),
                        ) {
                            FloatingToolbarDefaults.VibrantFloatingActionButton(
                                onClick = {
                                    onAction(Action.ShowRenameDialog)
                                },
                            ) {
                                Icon(Icons.Rounded.Edit, "Localized description")
                            }
                        }
                    },
                    content = {
                        AppBarRow(
                            overflowIndicator = {
                                TooltipBox(
                                    positionProvider =
                                        TooltipDefaults.rememberTooltipPositionProvider(
                                            TooltipAnchorPosition.Above
                                        ),
                                    tooltip = {
                                        PlainTooltip(
                                            modifier =
                                                Modifier.semantics {
                                                    liveRegion = LiveRegionMode.Assertive
                                                    paneTitle = "Overflow"
                                                }
                                        ) {
                                            Text("Overflow")
                                        }
                                    },
                                    state = rememberTooltipState(),
                                ) {
                                    IconButton(onClick = { it.show() }) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = "Overflow",
                                        )
                                    }
                                }
                            }
                        ) {
                            clickableItem(
                                onClick = {
                                    onAction(Action.ExportFile)
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.Download,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Download",
                            )
                            toggleableItem(
                                checked = uiState.isFavorite,
                                onCheckedChange = {
                                    onAction(Action.ToggleFavorite)
                                },
                                icon = {
                                    Icon(
                                        if (uiState.isFavorite)
                                            Icons.Rounded.Favorite
                                        else
                                            Icons.Rounded.FavoriteBorder,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Favorite",
                            )
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Rounded.Share,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Share",
                            )
                            clickableItem(
                                onClick = {
                                    onAction(Action.ShowDeleteDialog)
                                },
                                icon = {
                                    Icon(
                                        Icons.Rounded.Delete,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Delete",
                            )
                        }
                    },
                )
            }
            when (document.type) {
                DocumentType.IMAGE ->
                    ImageViewer(
                        file.getOrNull(),
                        modifier = Modifier.fillMaxSize(),
                        onToggleToolbar = {
                            onAction(Action.ToggleToolbar)
                        },
                        onShowInfo = {
                            onAction(Action.ShowInfoSheet)
                        }
                    )

                DocumentType.NOTE ->
                    ExpressiveTextField(
                        state = textFieldState,
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(16.dp),
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5),
                        label = {
                            Text(stringResource(R.string.note_content))
                        }
                    )

                else -> Unit
            }
        }
    }
    RenameDialog(
        visible = uiState.renameDialogVisible,
        documentName = document.name,
        onDismissRequest = {
            onAction(Action.HideRenameDialog)
        },
        onConfirm = { newName ->
            onAction(Action.Rename(newName))
        }
    )
    DeleteDialog(
        visible = uiState.deleteDialogVisible,
        onDismissRequest = {
            onAction(Action.HideDeleteDialog)
        },
        onConfirm = {
            onAction(Action.Delete)
        }
    )
    InfoSheet(
        visible = uiState.infoSheetVisible,
        document = document,
        onDismissRequest = {
            onAction(Action.HideInfoSheet)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoSheet(
    visible: Boolean,
    document: DocumentDto,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            contentWindowInsets = {
                BottomSheetDefaults.modalWindowInsets.add(WindowInsets(bottom = 16.dp))
            }
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TitleText(stringResource(R.string.file_info), Modifier.padding(bottom = 20.dp))
                InfoElement(
                    icon = Icons.Rounded.Title,
                    label = stringResource(R.string.document_name),
                    text = document.name,
                    index = 0,
                    count = 3
                )
                InfoElement(
                    icon = Icons.Rounded.Straighten,
                    label = stringResource(R.string.file_size),
                    text = document.size.formatFileSize(),
                    index = 1,
                    count = 3
                )
                InfoElement(
                    icon = Icons.Rounded.Today,
                    label = stringResource(R.string.document_created_at),
                    text = document.createdAt.toReadableDateString(),
                    index = 2,
                    count = 3
                )
            }
        }
    }
}

@Composable
internal fun InfoElement(
    icon: ImageVector,
    label: String,
    text: String,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = expressiveListItemShape(index, count),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun DocumentScreenPreview() {
    AppTheme {
        DocumentScreen(
            document = Resource.Success(
                DocumentDto(
                    id = "1",
                    name = "Document 1",
                    createdAt = System.currentTimeMillis(),
                    size = 1024L * 1024L * 5L,
                    tags = listOf(
                        TagDto("1", "Tag 1", System.currentTimeMillis()),
                        TagDto("2", "Tag 2", System.currentTimeMillis())
                    )
                )
            ),
            file = Resource.Success(Any()),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
            uiState = UiState(isFavorite = true)
        )
    }
}
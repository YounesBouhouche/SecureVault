package com.younesb.securevault.features.main.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.presentation.components.TitleText


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExportSheet(
    state: ExportState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onExport: (Boolean) -> Unit = {},
) {
    if (state !is ExportState.Initial) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            contentWindowInsets = {
                BottomSheetDefaults.modalWindowInsets.add(WindowInsets(bottom = 16.dp))
            },
            sheetState = rememberBottomSheetState(
                SheetValue.Expanded,
                enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
            )
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TitleText(stringResource(R.string.export_files), Modifier.padding(bottom = 20.dp))
                AnimatedContent(
                    state
                ) {
                    when(it) {
                        is ExportState.Idle -> {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                repeat(2) { index ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceContainer,
                                        shape = expressiveListItemShape(
                                            index,
                                            2,
                                            smallShape = MaterialTheme.shapes.small
                                        ),
                                        onClick = {
                                            onExport(index == 0)
                                        }
                                    ) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp, 24.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(
                                                    if (index == 0) R.drawable.ic_encrypted
                                                    else R.drawable.ic_file_export
                                                ),
                                                contentDescription = null,
                                                modifier = Modifier.size(48.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Column(
                                                Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(
                                                        if (index == 0) R.string.encrypted_export
                                                        else R.string.decrypted_export
                                                    ),
                                                    style = MaterialTheme.typography.titleLarge,
                                                )
                                                Text(
                                                    text = stringResource(
                                                        if (index == 0) R.string.encrypted_export_description
                                                        else R.string.decrypted_export_description
                                                    ),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is ExportState.Error -> {
                            Text(
                                text = it.message,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        ExportState.Exporting -> {
                            Box(modifier.size(200.dp), contentAlignment = Alignment.Center) {
                                LoadingIndicator(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .scale(1.2f),
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    polygons = listOf(
                                        MaterialShapes.Sunny,
                                        MaterialShapes.VerySunny,
                                        MaterialShapes.Cookie12Sided,
                                        MaterialShapes.Cookie4Sided,
                                    ),
                                )
                                Icon(
                                    Icons.Default.Inbox,
                                    null,
                                    modifier = Modifier.fillMaxSize(.3f),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        ExportState.Initial -> {}
                        ExportState.Success -> {
                            Text(
                                stringResource(R.string.export_success),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
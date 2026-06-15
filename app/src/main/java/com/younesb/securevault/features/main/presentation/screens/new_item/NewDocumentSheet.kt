package com.younesb.securevault.features.main.presentation.screens.new_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveButton
import com.younesb.securevault.core.presentation.components.ExpressiveDropdownMenu
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.components.Image
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.presentation.components.ModalSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDocumentSheet(
    modifier: Modifier = Modifier,
    onCreateFolder: () -> Unit = { }
) {
    val viewModel = koinViewModel<NewDocumentViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val nameTextFieldState = rememberTextFieldState(uiState.name)
    val descriptionTextFieldState = rememberTextFieldState()
    val noteTextFieldState = rememberTextFieldState()

    LaunchedEffect(uiState.name) {
        nameTextFieldState.setTextAndPlaceCursorAtEnd(uiState.name)
    }

    ModalSheet(
        visible = uiState.sheetVisible,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        onDismissRequest = {
            viewModel.onAction(NewDocumentAction.Dismiss)
        }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.type != DocumentType.NOTE) {
                Image(
                    model = uiState.fileUri,
                    icon = when(uiState.type) {
                        DocumentType.IMAGE -> Icons.Default.Image
                        else -> Icons.AutoMirrored.Filled.InsertDriveFile
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = MaterialTheme.shapes.extraLarge,
                    background = MaterialTheme.colorScheme.tertiaryContainer,
                    iconTint = MaterialTheme.colorScheme.tertiary
                )
            }
            ExpressiveTextField(
                state = nameTextFieldState,
                modifier = Modifier.fillMaxWidth().weight(2f),
                label = {
                    Text(stringResource(R.string.name))
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Checkbox(
                checked = uiState.selectedFolder != null,
                onCheckedChange = {
                    viewModel.onAction(
                        NewDocumentAction.SelectFolder(
                            if (it) 0 else null
                        )
                    )
                },
                enabled = uiState.folders.isNotEmpty()
            )
            ExpressiveDropdownMenu(
                modifier = Modifier.weight(1f),
                options = uiState.folders,
                selected = uiState.selectedFolder,
                onSelectionChange = {
                    viewModel.onAction(NewDocumentAction.SelectFolder(it))
                },
                enabled = uiState.folders.isNotEmpty() and (uiState.selectedFolder != null),
                text = { it?.name ?: "" },
                label = {
                    Text(stringResource(R.string.folder))
                }
            )
            IconButton(
                modifier = Modifier
                    .size(
                        IconButtonDefaults.mediumContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Narrow
                        )
                    ),
                onClick = onCreateFolder,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (uiState.type == DocumentType.NOTE) {
            ExpressiveTextField(
                state = noteTextFieldState,
                modifier = Modifier.fillMaxWidth(),
                lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5),
                label = {
                    Text(stringResource(R.string.note_content))
                }
            )
        } else {
            ExpressiveTextField(
                state = descriptionTextFieldState,
                modifier = Modifier.fillMaxWidth(),
                lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5),
                label = {
                    Text(stringResource(R.string.description))
                }
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExpressiveButton(
                text = stringResource(R.string.cancel),
                size = ButtonDefaults.MediumContainerHeight,
                outlined = true,
                onClick = {
                    viewModel.onAction(NewDocumentAction.Dismiss)
                },
                modifier = Modifier.weight(1f)
            )
            ExpressiveButton(
                text = stringResource(R.string.save),
                size = ButtonDefaults.MediumContainerHeight,
                onClick = {
                    viewModel.onAction(
                        NewDocumentAction.Confirm(
                            nameTextFieldState.text.toString(),
                            descriptionTextFieldState.text.toString(),
                            noteTextFieldState.text.toString(),
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

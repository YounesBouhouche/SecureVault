package com.younesb.securevault.features.main.presentation.screens.new_item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.components.Image
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.presentation.components.ModalSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDocumentSheet(modifier: Modifier = Modifier) {
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
        Image(
            model = uiState.fileUri,
            icon = when(uiState.type) {
                DocumentType.IMAGE -> Icons.Default.Image
                DocumentType.NOTE -> Icons.AutoMirrored.Filled.Note
                else -> Icons.AutoMirrored.Filled.InsertDriveFile
            },
            modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.extraLarge,
            background = MaterialTheme.colorScheme.tertiaryContainer,
            iconTint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(Modifier.height(24.dp))
        ExpressiveTextField(
            state = nameTextFieldState,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.name))
            }
        )
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
        Spacer(Modifier.height(16.dp))
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
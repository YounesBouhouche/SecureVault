package com.younesb.securevault.features.export.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.features.main.presentation.components.TitleText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExportScreen(
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
                    targetState = state,
                    contentKey = {
                        when(it) {
                            ExportState.Initial -> 0
                            is ExportState.Idle -> 1
                            ExportState.Exporting -> 2
                            ExportState.Success -> 3
                            is ExportState.Error -> 4
                        }
                    }
                ) {
                    when(it) {
                        is ExportState.Idle -> {
                            IdleContent(onExport = onExport, onCancel = onDismissRequest)
                        }

                        is ExportState.Error -> {
                            ErrorContent(error = it.message)
                        }

                        ExportState.Exporting -> {
                            ExportingContent()
                        }
                        ExportState.Initial -> {}
                        ExportState.Success -> {
                            SuccessContent(onClose = onDismissRequest)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExportScreenPreview() {
    var state by remember { mutableStateOf<ExportState>(ExportState.Idle(emptyList())) }
    val scope = rememberCoroutineScope()
    Surface(Modifier.fillMaxSize()) {
        ExportScreen(state = state, onDismissRequest = {
            state = ExportState.Initial
        }, onExport = {
            state = ExportState.Exporting
            scope.launch {
                delay(2000.milliseconds)
                state = ExportState.Success
            }
        })
    }
}

@Preview
@Composable
private fun ExportScreenLoadingPreview() {
    Surface(Modifier.fillMaxSize()) {
        ExportScreen(state = ExportState.Exporting, onDismissRequest = {
        }, onExport = {
        })
    }
}

@Preview
@Composable
private fun ExportScreenSuccessPreview() {
    Surface(Modifier.fillMaxSize()) {
        ExportScreen(state = ExportState.Success, onDismissRequest = {
        }, onExport = {
        })
    }
}
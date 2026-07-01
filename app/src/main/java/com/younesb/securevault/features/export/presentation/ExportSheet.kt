package com.younesb.securevault.features.export.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.younesb.securevault.features.export.presentation.util.CollectExportEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExportSheet(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ExportViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectExportEvents {
        viewModel.onAction(ExportAction.ShowExportSheet(it))
    }
    ExportScreen(
        state = state,
        modifier = modifier,
        onDismissRequest = {
            viewModel.onAction(ExportAction.HideExportSheet)
        },
        onExport = {
            viewModel.onAction(ExportAction.ExportFiles(it))
        }
    )
}
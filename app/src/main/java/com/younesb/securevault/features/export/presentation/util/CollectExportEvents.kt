package com.younesb.securevault.features.export.presentation.util

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.younesb.securevault.core.presentation.events.CollectEvents

@Composable
fun CollectExportEvents(
    onShowExportSheet: (List<String>?) -> Unit = {},
) {
    val saveFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("*/*")
    ) { result ->
        result?.let {
            SaveFileDialogManager.emitResult(it)
        }
    }
    CollectEvents(ExportEventsBus.events) {
        when (it) {
            is ExportEvent.ShowExportSheet -> {
                onShowExportSheet(it.documentsIds)
            }

            is ExportEvent.PickSavePath -> {
                saveFileLauncher.launch(it.fileName)
            }
        }
    }
}
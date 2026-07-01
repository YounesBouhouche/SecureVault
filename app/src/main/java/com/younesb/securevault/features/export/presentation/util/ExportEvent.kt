package com.younesb.securevault.features.export.presentation.util

sealed interface ExportEvent {
    data class ShowExportSheet(val documentsIds: List<String>?): ExportEvent
    data class PickSavePath(val fileName: String): ExportEvent
}
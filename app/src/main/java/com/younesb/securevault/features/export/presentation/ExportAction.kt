package com.younesb.securevault.features.export.presentation


sealed interface ExportAction {
    data class ShowExportSheet(val documentsIds: List<String>?): ExportAction
    data class ExportFiles(val encrypted: Boolean): ExportAction
    data object HideExportSheet: ExportAction
}
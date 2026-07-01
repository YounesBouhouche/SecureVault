package com.younesb.securevault.features.export.presentation

sealed class ExportState {
    data object Initial : ExportState()
    data class Idle(val ids: List<String>?) : ExportState()
    data object Exporting : ExportState()
    data object Success : ExportState()
    data class Error(val message: String) : ExportState()
}
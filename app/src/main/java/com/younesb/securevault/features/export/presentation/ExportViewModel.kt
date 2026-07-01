package com.younesb.securevault.features.export.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onError
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.export.presentation.util.SaveFileDialogManager
import com.younesb.securevault.features.export.domain.use_cases.ExportDocumentsUseCase
import com.younesb.securevault.features.main.presentation.util.MainEvent
import com.younesb.securevault.features.main.presentation.util.MainEventsBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ExportViewModel(
    val exportDocumentsUseCase: ExportDocumentsUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ExportState>(ExportState.Initial)
    val state = _state.asStateFlow()

    fun onAction(action: ExportAction) {
        when(action) {
            is ExportAction.ShowExportSheet -> {
                _state.value = ExportState.Idle(action.documentsIds)
            }
            is ExportAction.ExportFiles -> {
                val documentsIds = (_state.value as? ExportState.Idle ?: return).ids
                _state.value = ExportState.Exporting
                viewModelScope.launch {
                    MainEventsBus.sendEvent(MainEvent.PickSavePath("export_${LocalDateTime.now()}.zip"))
                    val uri = SaveFileDialogManager.results.first()
                    exportDocumentsUseCase(
                        documents = documentsIds,
                        destination = uri,
                        encrypted = action.encrypted
                    ).onSuccess {
                        _state.value = ExportState.Success
                    }.onError { error ->
                        error.printStackTrace()
                        _state.value = ExportState.Error(error.message ?: "")
                    }
                }
            }
            ExportAction.HideExportSheet -> {
                _state.value = ExportState.Initial
            }
        }
    }
}
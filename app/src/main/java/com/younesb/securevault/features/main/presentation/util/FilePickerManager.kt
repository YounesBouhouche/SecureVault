package com.younesb.securevault.features.main.presentation.util

import android.net.Uri
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FilePickerManager {
    val resultChannel = MutableSharedFlow<FileResult>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val fileResults = resultChannel.asSharedFlow()

    fun emitResult(uri: Uri?) {
        resultChannel.tryEmit(
            if(uri != null) FileResult.Success(uri)
            else FileResult.Error("No file selected")
        )
    }

    sealed interface FileResult {
        data class Success(val uri: Uri) : FileResult
        data class Error(val message: String) : FileResult
    }
}
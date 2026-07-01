package com.younesb.securevault.features.export.presentation.util

import android.net.Uri
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SaveFileDialogManager {
    val resultChannel = MutableSharedFlow<Uri>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val results = resultChannel.asSharedFlow()

    fun emitResult(uri: Uri) {
        resultChannel.tryEmit(uri)
    }
}
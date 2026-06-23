package com.younesb.securevault.features.main.presentation.util

import com.younesb.securevault.features.main.presentation.NewDocument
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object FilePickerManager {
    val resultChannel = MutableSharedFlow<NewDocument>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val fileResults = resultChannel.asSharedFlow()

    fun emitResult(document: NewDocument) {
        resultChannel.tryEmit(document)
    }
}
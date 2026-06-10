package com.younesb.securevault.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class Task {
    private var job: Job? = null

    // dedicated scope so launched coroutines are tied to a controllable Job
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // keep suspend signature for compatibility with callers; launching is done on internal scope
    fun start(block: suspend CoroutineScope.() -> Unit) {
        stop()
        if (scope.coroutineContext[Job]?.isCancelled == true) {
            throw IllegalStateException("Cannot start Task: underlying scope is cancelled")
        }
        job = scope.launch(block = block)
    }

    fun startRepeating(
        delay: Long,
        block: suspend CoroutineScope.() -> Unit,
    ) = start {
        while (isActive) {
            block()
            delay(delay.milliseconds)
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    // optional: cancel the underlying scope when Task is no longer needed
    fun cancelScope() {
        scope.cancel()
    }
}

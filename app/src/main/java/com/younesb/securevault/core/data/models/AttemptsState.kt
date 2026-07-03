package com.younesb.securevault.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AttemptsState(
    val durationMins: Int = 1,
    val maxAttempts: Int = 5,
    val attempts: Int = 0,
    val lastAttemptTime: Long = 0L
)
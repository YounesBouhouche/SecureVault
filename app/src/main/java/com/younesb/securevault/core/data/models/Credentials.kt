package com.younesb.securevault.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val biometricEnabled: Boolean = false,
    val pin: String = ""
)
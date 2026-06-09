package com.younesb.securevault.features.auth.presentation.util

import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes

sealed interface Event {
    data class ShowBiometricPrompt(
        val title: String,
        val description: String,
    ): Event

    data object LaunchScreenLockSettings: Event

    data class Navigate(val route: AuthRoutes): Event
}
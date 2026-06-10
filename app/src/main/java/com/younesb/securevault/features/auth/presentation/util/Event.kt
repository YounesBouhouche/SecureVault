package com.younesb.securevault.features.auth.presentation.util

import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.navigation.NavRoutes

sealed interface Event {
    data class ShowBiometricPrompt(
        val title: String,
        val description: String,
    ): Event

    data object LaunchScreenLockSettings: Event

    data class AuthNavigate(val route: AuthRoutes): Event

    data class Navigate(val route: NavRoutes): Event
}
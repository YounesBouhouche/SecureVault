package com.younesb.securevault.features.auth.presentation.util

import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.navigation.NavRoutes

sealed interface AuthEvent {
    data class ShowBiometricPrompt(
        val title: String,
        val description: String,
    ): AuthEvent

    data object LaunchScreenLockSettings: AuthEvent

    data class AuthNavigate(val route: AuthRoutes): AuthEvent

    data class Navigate(val route: NavRoutes): AuthEvent
}
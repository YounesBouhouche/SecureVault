package com.younesb.securevault.features.auth.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes {
    @Serializable
    data object Onboarding: AuthRoutes()
}
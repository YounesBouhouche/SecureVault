package com.younesb.securevault.features.auth.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes {
    @Serializable
    data object Onboarding: AuthRoutes()
    @Serializable
    data object Setup: AuthRoutes()
    @Serializable
    data object SetupPin: AuthRoutes()
    @Serializable
    data object FinishSetup: AuthRoutes()
    @Serializable
    data object EnterPin: AuthRoutes()
}
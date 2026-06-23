package com.younesb.securevault.features.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
    @Serializable
    data object Auth: NavRoutes()
    @Serializable
    data object Main: NavRoutes()
    @Serializable
    data object Settings: NavRoutes()
}
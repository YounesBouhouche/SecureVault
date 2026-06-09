package com.younesb.securevault.features.main.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Home: MainRoutes()
}
package com.younesb.securevault.features.main.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Home: MainRoutes()
    @Serializable
    data object Browse: MainRoutes()
    @Serializable
    data object Export: MainRoutes()
}
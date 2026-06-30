package com.younesb.securevault.features.main.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {
    @Serializable
    data object Home: MainRoutes()
    @Serializable
    data object Browse: MainRoutes()
    @Serializable
    data class Folder(val folderId: String): MainRoutes()
    @Serializable
    data class Document(val documentId: String): MainRoutes()
    @Serializable
    data object Settings: MainRoutes()
    @Serializable
    data object ChangePassword: MainRoutes()
    @Serializable
    data object About: MainRoutes()
}
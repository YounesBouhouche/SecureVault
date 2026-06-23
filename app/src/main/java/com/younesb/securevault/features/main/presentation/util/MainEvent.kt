package com.younesb.securevault.features.main.presentation.util

import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes

sealed interface MainEvent {
    data object PickFile: MainEvent
    data object PickPicture: MainEvent
    data object TakePicture: MainEvent
    data object RequestNewNote: MainEvent
    data class MainNavigate(val route: AuthRoutes): MainEvent
    data object MainPopBackStack: MainEvent
}
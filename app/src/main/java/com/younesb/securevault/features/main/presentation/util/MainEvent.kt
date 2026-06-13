package com.younesb.securevault.features.main.presentation.util

import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.navigation.NavRoutes

sealed interface MainEvent {
    data object PickFile: MainEvent
    data object PickPicture: MainEvent
    data object TakePicture: MainEvent
    data class MainNavigate(val route: AuthRoutes): MainEvent
}
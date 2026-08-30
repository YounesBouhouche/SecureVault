package com.younesb.securevault.features.main.presentation.navigation

import com.younesb.securevault.R

enum class MainNavRoutes(
    val icon: Int,
    val label: Int,
    val destination: MainRoutes
) {
    HOME(
        icon = R.drawable.home_filled,
        label = R.string.home,
        destination = MainRoutes.Home
    ),
    SETTINGS(
        icon = R.drawable.settings_filled,
        label = R.string.settings,
        destination = MainRoutes.Settings
    ),
}
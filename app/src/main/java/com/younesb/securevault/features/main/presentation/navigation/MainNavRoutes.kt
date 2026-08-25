package com.younesb.securevault.features.main.presentation.navigation

import com.younesb.securevault.R

enum class MainNavRoutes(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val label: Int,
    val destination: MainRoutes
) {
    HOME(
        selectedIcon = R.drawable.home_filled,
        unselectedIcon = R.drawable.home_outlined,
        label = R.string.home,
        destination = MainRoutes.Home
    ),
    SETTINGS(
        selectedIcon = R.drawable.settings_filled,
        unselectedIcon = R.drawable.settings_outlined,
        label = R.string.settings,
        destination = MainRoutes.Settings
    ),
}
package com.younesb.securevault.features.main.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.younesb.securevault.features.main.presentation.screens.MainScreen
import com.younesb.securevault.features.navigation.NavRoutes

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
) = composable<NavRoutes.Main> {
    MainScreen()
}

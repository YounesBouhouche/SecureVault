package com.younesb.securevault.features.main.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.younesb.securevault.features.navigation.NavRoutes

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
) {
    return navigation<NavRoutes.Main>(MainRoutes.Home) {
        composable<MainRoutes.Home> {
            Button({}) {
                Text("Log out")
            }
        }
    }
}
package com.younesb.securevault.features.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.younesb.securevault.features.navigation.NavRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
) {
    return navigation<NavRoutes.Auth>(AuthRoutes.Onboarding) {
        composable<AuthRoutes.Onboarding> {

        }
    }
}
package com.younesb.securevault.features.auth.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.younesb.securevault.features.auth.presentation.screens.OnboardingScreen
import com.younesb.securevault.features.auth.presentation.screens.SetupFinishedScreen
import com.younesb.securevault.features.auth.presentation.screens.SetupPinScreen
import com.younesb.securevault.features.auth.presentation.screens.SetupScreen
import com.younesb.securevault.features.navigation.NavRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
) {
    return navigation<NavRoutes.Auth>(AuthRoutes.Onboarding) {
        composable<AuthRoutes.Onboarding> {
            OnboardingScreen(
                sharedTransitionScope,
                this,
            ) {
                navController.navigate(AuthRoutes.Setup)
            }
        }
        composable<AuthRoutes.Setup> {
            SetupScreen(
                sharedTransitionScope,
                this,
            )
        }
        composable<AuthRoutes.SetupPin> {
            SetupPinScreen(
                sharedTransitionScope,
                this,
            )
        }
        composable<AuthRoutes.FinishSetup> {
            SetupFinishedScreen(
                sharedTransitionScope,
                this,
            ) {
                navController.navigate(NavRoutes.Main) {
                    popUpTo(NavRoutes.Auth) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
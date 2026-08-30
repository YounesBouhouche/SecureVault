package com.younesb.securevault.features.auth.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.younesb.securevault.features.auth.presentation.screens.AuthPinScreen
import com.younesb.securevault.features.auth.presentation.screens.OnboardingScreen
import com.younesb.securevault.features.auth.presentation.screens.SetupPinScreen
import com.younesb.securevault.features.navigation.NavRoutes

context(sharedTransitionScope: SharedTransitionScope)
fun NavGraphBuilder.authNavGraph() {
    return navigation<NavRoutes.Auth>(AuthRoutes.Onboarding) {
        composable<AuthRoutes.Onboarding> {
            OnboardingScreen()
        }
        composable<AuthRoutes.SetupPin> {
            SetupPinScreen()
        }
        composable<AuthRoutes.EnterPin> {
            AuthPinScreen()
        }
    }
}
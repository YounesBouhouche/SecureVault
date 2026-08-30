package com.younesb.securevault.features.main.presentation.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.younesb.securevault.features.main.presentation.screens.MainScreen
import com.younesb.securevault.features.navigation.NavRoutes

context(sharedTransitionScope: SharedTransitionScope)
fun NavGraphBuilder.mainNavGraph() = composable<NavRoutes.Main> {
    with(sharedTransitionScope) {
        MainScreen(
            Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "onboarding-setup"),
                animatedVisibilityScope = this@composable,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
            )
        )
    }
}

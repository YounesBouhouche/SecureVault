package com.younesb.securevault.features.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.features.auth.presentation.navigation.authNavGraph
import com.younesb.securevault.features.main.presentation.navigation.mainNavGraph

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    SharedTransitionLayout(modifier) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Auth,
            modifier = modifier
        ) {
            authNavGraph(
                navController = navController,
                sharedTransitionScope = this@SharedTransitionLayout,
            )
            mainNavGraph(
                navController = navController,
                sharedTransitionScope = this@SharedTransitionLayout,
            )
        }
    }
}

@Preview
@Composable
private fun NavigationHostPreview() {
    NavigationHost()
}
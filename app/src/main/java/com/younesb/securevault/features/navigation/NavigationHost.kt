package com.younesb.securevault.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.features.auth.presentation.navigation.authNavGraph

@Composable
fun NavigationHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Auth,
        modifier = modifier
    ) {
        authNavGraph(
            navController = navController,
        )
    }
}

@Preview
@Composable
private fun NavigationHostPreview() {
    NavigationHost()
}
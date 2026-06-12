package com.younesb.securevault.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.features.main.presentation.navigation.MainNavRoutes
import com.younesb.securevault.features.main.presentation.navigation.MainNavigationBar
import com.younesb.securevault.features.main.presentation.navigation.MainNavigationHost
import com.younesb.securevault.features.main.presentation.navigation.MainSearchBar
import com.younesb.securevault.features.main.presentation.navigation.util.getCurrentRoute
import com.younesb.securevault.features.navigation.NavRoutes

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navigate: (NavRoutes) -> Unit = { },
) {
    val navController = rememberNavController()
    val route = navController.getCurrentRoute(MainNavRoutes.entries) {
        it.destination
    }
    val isParentRoute = route != null
    Box(modifier.fillMaxSize()) {
        MainNavigationHost(navController = navController)
        MainSearchBar(modifier = Modifier.align(Alignment.TopCenter)) {
            navigate(NavRoutes.Settings)
        }
        MainNavigationBar(
            route = route,
            modifier = Modifier.align(Alignment.BottomCenter),
            navigate = {
                navController.navigate(it) {
                    launchSingleTop = true
                    popUpTo(it) { inclusive = true }
                }
            },
            visible = isParentRoute
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    AppTheme {
        Surface {
            MainScreen()
        }
    }
}
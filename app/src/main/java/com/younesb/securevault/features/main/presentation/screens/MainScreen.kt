package com.younesb.securevault.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.features.export.presentation.ExportSheet
import com.younesb.securevault.features.main.presentation.components.MainNavigationBar
import com.younesb.securevault.features.main.presentation.navigation.MainNavRoutes
import com.younesb.securevault.features.main.presentation.navigation.MainNavigationHost
import com.younesb.securevault.features.main.presentation.navigation.util.getCurrentRoute
import com.younesb.securevault.features.main.presentation.util.CollectMainEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val route = navController.getCurrentRoute(MainNavRoutes.entries) {
        it.destination
    }
    val isParentRoute = route != null
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    CollectMainEvents(navController = navController)

    Surface(modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            MainNavigationHost(navController = navController)
            MainNavigationBar(
                newDocState = uiState.newDocState,
                onStateChange = { viewModel.onAction(Action.UpdateNewDocState(it)) },
                onConfirm = { name, description ->
                    viewModel.onAction(Action.CreateNewDoc(name, description))
                            },
                route = route,
                modifier = Modifier.align(Alignment.BottomCenter),
                navigate = {
                    navController.navigate(it) {
                        launchSingleTop = true
                        popUpTo(it) { inclusive = true }
                    }
                },
                visible = isParentRoute,
            )
        }
    }
    ExportSheet()
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
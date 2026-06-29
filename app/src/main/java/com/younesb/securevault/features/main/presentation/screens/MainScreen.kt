package com.younesb.securevault.features.main.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.features.main.presentation.components.MainNavigationBar
import com.younesb.securevault.features.main.presentation.components.MainSearchBar
import com.younesb.securevault.features.main.presentation.navigation.MainNavRoutes
import com.younesb.securevault.features.main.presentation.navigation.MainNavigationHost
import com.younesb.securevault.features.main.presentation.navigation.util.getCurrentRoute
import com.younesb.securevault.features.main.presentation.screens.new_item.NewDocumentSheet
import com.younesb.securevault.features.main.presentation.util.CollectMainEvents
import com.younesb.securevault.features.navigation.NavRoutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val route = navController.getCurrentRoute(MainNavRoutes.entries) {
        it.destination
    }
    val isParentRoute = route != null
    val searchBarVisible = isParentRoute and (route != MainNavRoutes.SETTINGS)
    val contentPadding =
        WindowInsets.statusBars.add(WindowInsets(top = 80.dp, bottom = 200.dp)).asPaddingValues()
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val backStack = navController.currentBackStackEntryAsState().value
    LaunchedEffect(backStack) {
        println("Current route: ${backStack?.destination?.route}")
    }

    CollectMainEvents(navController = navController)

    Box(modifier.fillMaxSize()) {
        MainNavigationHost(navController = navController, contentPadding = contentPadding)
        MainSearchBar(modifier = Modifier.align(Alignment.TopCenter), visible = searchBarVisible)
        MainNavigationBar(
            route = route,
            modifier = Modifier.align(Alignment.BottomCenter),
            navigate = {
                navController.navigate(it) {
                    launchSingleTop = true
                    popUpTo(it) { inclusive = true }
                }
            },
            onNewItemAction = {
                viewModel.onAction(Action.ShowFilePicker(it))
            },
            visible = isParentRoute
        )
    }

    NewDocumentSheet(
        onCreateFolder = {
            viewModel.onAction(Action.ShowNewFolderDialog)
        },
        onCreateTag = {
            viewModel.onAction(Action.ShowNewTagDialog)
        }
    )

    NewFolderDialog(
        visible = uiState.showNewFolderDialog,
        onDismissRequest = { viewModel.onAction(Action.HideNewFolderDialog) },
        onConfirm = {
            viewModel.onAction(Action.CreateFolder(it))
        }
    )

    NewTagDialog(
        visible = uiState.showNewTagDialog,
        onDismissRequest = { viewModel.onAction(Action.HideNewTagDialog) },
        onConfirm = {
            viewModel.onAction(Action.CreateTag(it))
        }
    )
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
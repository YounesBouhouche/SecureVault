package com.younesb.securevault.features.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.younesb.securevault.features.auth.presentation.navigation.authNavGraph
import com.younesb.securevault.features.auth.presentation.util.meshPainter
import com.younesb.securevault.features.main.presentation.navigation.mainNavGraph
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val slideDistance = rememberSlideDistance()
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Auth,
            modifier = modifier
                .meshPainter(
                    color1 = MaterialTheme.colorScheme.surfaceContainerLowest,
                    color2 = MaterialTheme.colorScheme.surfaceContainerLow,
                    color3 = MaterialTheme.colorScheme.primaryContainer,
                    color4 = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            enterTransition = {
                materialSharedAxisYIn(forward = true, slideDistance = slideDistance)
            },
            exitTransition = {
                materialSharedAxisYOut(forward = false, slideDistance = slideDistance)
            },
            popEnterTransition = {
                materialSharedAxisYIn(forward = true, slideDistance = slideDistance)
            },
            popExitTransition = {
                materialSharedAxisYOut(forward = false, slideDistance = slideDistance)
            },
            predictivePopEnterTransition = {
                materialSharedAxisYIn(forward = true, slideDistance = slideDistance)
            },
            predictivePopExitTransition = {
                materialSharedAxisYOut(forward = false, slideDistance = slideDistance)
            },
        ) {
            authNavGraph()
            mainNavGraph()
        }
    }
}

@Preview
@Composable
private fun NavigationHostPreview() {
    NavigationHost()
}
package com.younesb.securevault.features.main.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
fun MainNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val slideDistance = rememberSlideDistance()
    SharedTransitionLayout(modifier) {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Home,
            enterTransition = {
                materialSharedAxisYIn(forward = true, slideDistance = slideDistance)
            },
            exitTransition = {
                materialSharedAxisYOut(forward = true, slideDistance = slideDistance)
            },
            modifier = modifier
        ) {
            composable<MainRoutes.Home> {

            }
            composable<MainRoutes.Browse> {

            }
            composable<MainRoutes.Export> {

            }
        }
    }
}

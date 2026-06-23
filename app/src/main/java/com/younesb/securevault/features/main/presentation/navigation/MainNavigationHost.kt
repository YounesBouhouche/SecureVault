package com.younesb.securevault.features.main.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.younesb.securevault.features.main.presentation.navigation.routes.browse.BrowseScreen
import com.younesb.securevault.features.main.presentation.navigation.routes.document.DocumentRoute
import com.younesb.securevault.features.main.presentation.navigation.routes.folder.FolderScreen
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
fun MainNavigationHost(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
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
                materialSharedAxisYOut(forward = false, slideDistance = slideDistance)
            },
            modifier = modifier
        ) {
            composable<MainRoutes.Home> {

            }
            composable<MainRoutes.Browse> {
                BrowseScreen(
                    contentPadding = contentPadding,
                    onFolderClick = { id ->
                        navController.navigate(MainRoutes.Folder(folderId = id))
                    },
                    onDocumentClick = { id ->
                        navController.navigate(MainRoutes.Document(documentId = id))
                    }
                )
            }
            composable<MainRoutes.Folder> {
                val folderId = it.toRoute<MainRoutes.Folder>().folderId
                FolderScreen(
                    folderId = folderId,
                    onDocumentClick = { id ->
                        navController.navigate(MainRoutes.Document(documentId = id))
                    }
                )
            }
            composable<MainRoutes.Document> {
                val documentId = it.toRoute<MainRoutes.Document>().documentId
                DocumentRoute(documentId = documentId)
            }
            composable<MainRoutes.Settings> {

            }
        }
    }
}

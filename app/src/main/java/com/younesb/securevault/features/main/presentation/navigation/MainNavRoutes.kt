package com.younesb.securevault.features.main.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Outbox
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Outbox
import androidx.compose.material.icons.rounded.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.younesb.securevault.R

enum class MainNavRoutes(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: Int,
    val destination: MainRoutes
) {
    HOME(
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = R.string.home,
        destination = MainRoutes.Home
    ),
    BROWSE(
        selectedIcon = Icons.Rounded.Folder,
        unselectedIcon = Icons.Outlined.Folder,
        label = R.string.browse,
        destination = MainRoutes.Browse
    ),
    EXPORT(
        selectedIcon = Icons.Rounded.Outbox,
        unselectedIcon = Icons.Outlined.Outbox,
        label = R.string.export,
        destination = MainRoutes.Export
    ),
}
package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarDefaults
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.FabMenu
import com.younesb.securevault.features.main.presentation.navigation.MainNavRoutes
import com.younesb.securevault.features.main.presentation.navigation.MainRoutes
import com.younesb.securevault.features.main.presentation.util.NewItemType
import soup.compose.material.motion.animation.materialSharedAxisZ

@Composable
fun MainNavigationBar(
    route: MainNavRoutes?,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onNewItemAction: (NewItemType) -> Unit = { },
    navigate: (MainRoutes) -> Unit = { },
    onExport: () -> Unit = { }
) {
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val resources = LocalResources.current

    Column(modifier) {
        FloatingActionButton(
            modifier =
                Modifier
                    .semantics {
                        traversalIndex = -1f
                        stateDescription =
                            resources.getString(R.string.export)
                        contentDescription =
                            resources.getString(R.string.export)
                    }
                    .animateFloatingActionButton(
                        visible = visible,
                        alignment = Alignment.BottomEnd
                    )
                    .align(Alignment.End)
                    .padding(16.dp),
            onClick = onExport,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Rounded.Share),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
        }
        FabMenu(
            actions = NewItemType.entries,
            icon = { it.icon },
            text = { stringResource(it.textRes) },
            modifier = Modifier.align(Alignment.End),
            expanded = fabMenuExpanded,
            visible = visible,
            onExpandedChange = { fabMenuExpanded = it }
        ) {
            onNewItemAction(it)
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
            exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
        ) {
            ShortNavigationBar(
                windowInsets = ShortNavigationBarDefaults.windowInsets.add(
                    WindowInsets(left = 8.dp, right = 8.dp)
                )
            ) {
                MainNavRoutes.entries.forEach {
                    val selected = route == it
                    ShortNavigationBarItem(
                        selected = selected,
                        iconPosition = NavigationItemIconPosition.Start,
                        icon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AnimatedContent(
                                    selected,
                                    transitionSpec = {
                                        materialSharedAxisZ(targetState)
                                    }
                                ) { isSelected ->
                                    Icon(
                                        if (isSelected) it.selectedIcon
                                        else it.unselectedIcon,
                                        null,
                                    )
                                }
                                AnimatedVisibility(
                                    selected,
                                    enter = expandIn(
                                        expandFrom = Alignment.CenterStart,
                                        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                                    ),
                                    exit = shrinkOut(
                                        shrinkTowards = Alignment.CenterStart,
                                        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                                    )
                                ) {
                                    Text(
                                        stringResource(it.label),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 4.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        softWrap = false,
                                        autoSize = TextAutoSize.StepBased(
                                            maxFontSize = MaterialTheme.typography.labelMedium.fontSize
                                        )
                                    )
                                }
                            }
                        },
                        label = null,
                        onClick = {
                            navigate(it.destination)
                        },
                        colors = ShortNavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(.2f),
                        )
                    )
                }
            }
        }
    }
}


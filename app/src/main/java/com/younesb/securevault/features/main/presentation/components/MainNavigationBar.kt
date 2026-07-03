package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
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
    exportButtonVisible: Boolean = true,
    onNewItemAction: (NewItemType) -> Unit = { },
    navigate: (MainRoutes) -> Unit = { },
    onExport: () -> Unit = { }
) {
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val resources = LocalResources.current
    val fabVisible = visible and (route == MainNavRoutes.HOME)
    val density = LocalDensity.current
    var navbarWidth by remember {
        mutableStateOf(0.dp)
    }
    val itemSize = navbarWidth / MainNavRoutes.entries.size
    val offset = itemSize * (route?.ordinal ?: 0)
    val animatedOffset by animateDpAsState(offset)

    Column(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
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
                        visible = fabVisible and exportButtonVisible,
                        alignment = Alignment.BottomEnd
                    )
                    .align(Alignment.End)
                    .padding(16.dp),
            onClick = onExport,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Icon(
                imageVector = Icons.Rounded.Upload,
                contentDescription = null,
            )
        }
        FabMenu(
            actions = NewItemType.entries,
            icon = { it.icon },
            text = { stringResource(it.textRes) },
            modifier = Modifier.align(Alignment.End),
            expanded = fabMenuExpanded,
            visible = fabVisible,
            onExpandedChange = { fabMenuExpanded = it }
        ) {
            onNewItemAction(it)
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
            exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { it },
        ) {
            HorizontalFloatingToolbar(
                expanded = true,
                contentPadding = PaddingValues(10.dp, 8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                expandedShadowElevation = 4.dp,
                shape = RoundedCornerShape(100)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .onGloballyPositioned {
                            with(density) {
                                navbarWidth = it.size.width.toDp()
                            }
                        }
                        .onSizeChanged {
                            with(density) {
                                navbarWidth = it.width.toDp()
                            }
                        }
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(.2f),
                        modifier = Modifier
                            .width(itemSize)
                            .fillMaxHeight()
                            .offset {
                                IntOffset(x = animatedOffset.roundToPx(), y = 0)
                            },
                        content = {},
                        shape = RoundedCornerShape(100)
                    )
                    Row(Modifier.fillMaxWidth()) {
                        MainNavRoutes.entries.forEach {
                            val selected = route == it
                            val color by animateColorAsState(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                color = Color.Transparent,
                                contentColor = color,
                                shape = MaterialTheme.shapes.extraLarge,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navigate(it.destination)
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        4.dp,
                                        Alignment.CenterHorizontally
                                    )
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
                        }
                    }
                }
            }
        }
    }
}


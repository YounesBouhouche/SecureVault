package com.younesb.securevault.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import com.younesb.securevault.R

@Composable
fun <T> FabMenu(
    actions: List<T>,
    icon: @Composable (T) -> ImageVector,
    text: @Composable (T) -> String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onExpandedChange: (Boolean) -> Unit,
    onActionClick: (T) -> Unit,
) {
    val resources = LocalResources.current
    val focusRequester = remember { FocusRequester() }
    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        button = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(
                        if (expanded) {
                            TooltipAnchorPosition.Start
                        } else {
                            TooltipAnchorPosition.Above
                        }
                    ),
                tooltip = {
                    PlainTooltip(
                        modifier =
                            Modifier.semantics {
                                liveRegion = LiveRegionMode.Assertive
                                paneTitle = resources.getString(R.string.toggle_menu)
                            }
                    ) {
                        Text(stringResource(R.string.toggle_menu))
                    }
                },
                state = rememberTooltipState(),
            ) {
                ToggleFloatingActionButton(
                    containerSize = ToggleFloatingActionButtonDefaults.containerSizeMedium(),
                    modifier =
                        Modifier
                            .semantics {
                                traversalIndex = -1f
                                stateDescription =
                                    resources.getString(
                                        if (expanded) R.string.expanded
                                        else R.string.collapsed
                                    )
                                contentDescription =
                                    resources.getString(R.string.toggle_menu)
                            }
                            .animateFloatingActionButton(
                                visible = expanded || visible,
                                alignment = Alignment.BottomEnd
                            ),
                    checked = expanded,
                    onCheckedChange = { onExpandedChange(!expanded) },
                    containerColor = ToggleFloatingActionButtonDefaults.containerColor(),
                    containerCornerRadius = ToggleFloatingActionButtonDefaults.containerCornerRadiusMedium(),
                ) {
                    val imageVector by remember {
                        derivedStateOf {
                            if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                        }
                    }
                    Icon(
                        painter = rememberVectorPainter(imageVector),
                        contentDescription = null,
                        modifier = Modifier.animateIcon(
                            checkedProgress = { checkedProgress },
                            color = ToggleFloatingActionButtonDefaults.iconColor(),
                            size = ToggleFloatingActionButtonDefaults.iconSizeMedium(),
                        ),
                    )
                }
            }
        },
    ) {
        actions.forEachIndexed { i, item ->
            FloatingActionButtonMenuItem(
                modifier =
                    Modifier
                        .semantics {
                            isTraversalGroup = true
                            if (i == actions.size - 1) {
                                customActions =
                                    listOf(
                                        CustomAccessibilityAction(
                                            label = resources.getString(R.string.close_menu),
                                            action = {
                                                onExpandedChange(false)
                                                true
                                            },
                                        )
                                    )
                            }
                        }
                        .then(
                            if (i == 0) {
                                Modifier.onKeyEvent {
                                    if (
                                        it.type == KeyEventType.KeyDown &&
                                        (it.key == Key.DirectionUp ||
                                                it.key == Key.NumPadDirectionUp ||
                                                (it.isShiftPressed && it.key == Key.Tab))
                                    ) {
                                        focusRequester.requestFocus()
                                        return@onKeyEvent true
                                    }
                                    return@onKeyEvent false
                                }
                            } else {
                                Modifier
                            }
                        ),
                onClick = {
                    onActionClick(item)
                    onExpandedChange(false)
                },
                icon = { Icon(icon(item), contentDescription = null) },
                text = { Text(text = text(item)) },
            )
        }
    }
}
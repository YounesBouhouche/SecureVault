package com.younesb.securevault.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveButtonsGroup(
    count: () -> Int,
    label: @Composable (index: Int) -> String,
    checked: (index: Int) -> Boolean,
    icons: (index: Int, checked: Boolean) -> ImageVector?,
    onCheckedChange: (index: Int, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val size = remember { count() }
    val checked = remember {
        List(size) { index -> checked(index) }.toMutableStateList()
    }
    val interactionSources = remember {
        List(size) { MutableInteractionSource() }
    }
    ButtonGroup(
        modifier = modifier,
        overflowIndicator = { menuState ->
            ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
        },
        expandedRatio = 1f,
    ) {
        repeat(size) { index ->
            val icon = icons(index, checked[index])
            customItem(
                buttonGroupContent = {
                    ToggleButton(
                        checked = checked[index],
                        onCheckedChange = { onCheckedChange(index, !it) },
                        shapes =
                            when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                size - 1 ->
                                    ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        interactionSource = interactionSources[index],
                        modifier =
                            Modifier.animateWidth(
                                interactionSource = interactionSources[index],
                                compressionLimit = ButtonDefaults.ButtonWithIconContentPadding,
                            ),
                    ) {
                        AnimatedVisibility(icon != null) {
                            Row {
                                AnimatedContent(icon) {
                                    it?.let {
                                        Icon(
                                            it,
                                            contentDescription = null,
                                            modifier = Modifier.size(ToggleButtonDefaults.IconSize)
                                        )
                                    }
                                }
                                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                            }
                        }
                        Text(
                            text = label(index),
                            softWrap = false,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                        )
                    }
                },
                menuContent = {
                    DropdownMenuItem(
                        leadingIcon = icons(index, true)?.let {
                            { Icon(it, contentDescription = null) }
                        },
                        text = { Text(label(index)) },
                        onClick = {},
                        interactionSource = interactionSources[index],
                    )
                },
            )
        }
    }
}
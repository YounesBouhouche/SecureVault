package com.younesb.securevault.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CheckableDropdownMenuItem
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.util.fastForEachIndexed


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun<T> ExpressiveDropdownMenu(
    items: List<Pair<String?, List<T>>>,
    checked: (T) -> Boolean,
    onCheckedChange: (T, Boolean) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (T) -> String,
    supportingText: @Composable ((T) -> String?)? = null,
    leadingIcon: @Composable ((T) -> ImageVector?)? = null,
    checkedLeadingIcon: @Composable ((T) -> ImageVector)? = null,
    trailingIcon: @Composable ((T) -> ImageVector?)? = null,
    checkedTrailingIcon: @Composable ((T) -> ImageVector?)? = null,
    content: @Composable (() -> Unit),
) {
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        content()
        DropdownMenuPopup(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            items.fastForEachIndexed { groupIndex, (label, groupItems) ->
                DropdownMenuGroup(
                    shapes = MenuDefaults.groupShape(groupIndex, items.size),
                ) {
                    label?.let {
                        MenuDefaults.DropdownMenuGroupLabel { Text(it) }
                        HorizontalDivider(
                            modifier = Modifier.padding(MenuDefaults.HorizontalDividerPadding)
                        )
                    }
                    val groupItemCount = groupItems.size
                    groupItems.fastForEachIndexed { itemIndex, item ->
                        CheckableDropdownMenuItem(
                            text = { Text(label(item)) },
                            supportingText =
                                supportingText?.invoke(item)?.let { supportingText ->
                                    { Text(supportingText) }
                                },
                            shapes = MenuDefaults.itemShape(itemIndex, groupItemCount),
                            leadingIcon =
                                leadingIcon?.invoke(item)?.let { iconData ->
                                    {
                                        Icon(
                                            iconData,
                                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                            contentDescription = null,
                                        )
                                    }
                                },
                            checkedLeadingIcon =
                                checkedLeadingIcon?.invoke(item)?.let { icon ->
                                    {
                                        Icon(
                                            imageVector = icon,
                                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                            contentDescription = null,
                                        )
                                    }
                                },
                            trailingContent =
                                if (checked(item)) {
                                    checkedTrailingIcon?.invoke(item)?.let { icon ->
                                        {
                                            Icon(
                                                imageVector = icon,
                                                modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                } else {
                                    trailingIcon?.invoke(item)?.let { icon ->
                                        {
                                            Icon(
                                                imageVector = icon,
                                                modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                },
                            checked = checked(item),
                            onCheckedChange = { onCheckedChange(item, it) },
                        )
                    }
                }

                if (groupIndex != items.size - 1) {
                    Spacer(Modifier.height(MenuDefaults.GroupSpacing))
                }
            }
        }
    }
}
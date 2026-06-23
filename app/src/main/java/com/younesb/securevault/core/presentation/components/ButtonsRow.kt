package com.younesb.securevault.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A row of equally-weighted buttons with animated press states.
 * When a button is pressed, it smoothly expands while others shrink, creating an expressive interaction.
 *
 * This component is useful for choice selections, segmented controls, or action groups
 * where you need multiple buttons with consistent styling.
 *
 * Features:
 * - Animated weight distribution on press
 * - Configurable button count with index-based customization
 * - Per-button icon, text, and styling
 * - Uses [ExpressiveButton] internally
 *
 * @param count The number of buttons to display.
 * @param icon Function that returns the icon for button at given index (can return null).
 * @param text Function that returns the text for button at given index.
 * @param modifier The modifier to be applied to the row.
 * @param outlined Function that returns whether button at given index should be outlined.
 * @param enabled Function that returns whether button at given index should be enabled.
 * @param colors Function that returns the colors for button at given index.
 * @param size The height of each button.
 * @param expandedWeight The weight multiplier for the pressed button (default 1.15x).
 * @param verticalAlignment Vertical alignment of buttons in the row.
 * @param horizontalArrangement Horizontal arrangement and spacing of buttons.
 * @param onClick Callback invoked when a button is clicked, receives the button index.
 *
 * @sample
 * ```
 * ButtonsRow(
 *     count = 3,
 *     icon = { index ->
 *         when(index) {
 *             0 -> Icons.Default.Home
 *             1 -> Icons.Default.Search
 *             else -> Icons.Default.Settings
 *         }
 *     },
 *     text = { index -> listOf("Home", "Search", "Settings")[index] },
 *     outlined = { index -> index != selectedIndex },
 *     onClick = { index -> selectedIndex = index }
 * )
 * ```
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonsRow(
    count: Int,
    icon: @Composable (Int) -> ImageVector?,
    text: @Composable (Int) -> String,
    modifier: Modifier = Modifier,
    outlined: (Int) -> Boolean = { false },
    enabled: @Composable (Int) -> Boolean = { true },
    colors: @Composable (Int) -> ButtonColors = {
        if (outlined(it))
            ButtonDefaults.outlinedButtonColors()
        else
            ButtonDefaults.buttonColors()
                                                },
    size: Dp = ButtonDefaults.MediumContainerHeight,
    expandedWeight: Float = 1.15f,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    buttonContentPaddingValues: PaddingValues = ButtonDefaults.contentPaddingFor(size),
    onClick: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(count) {
            val interactionSource = remember {
                MutableInteractionSource()
            }
            val pressed by interactionSource.collectIsPressedAsState()
            val weight by animateFloatAsState(
                if (pressed) expandedWeight else 1f,
                MaterialTheme.motionScheme.defaultEffectsSpec()
            )
            ExpressiveButton(
                text = text(it),
                icon = icon(it),
                size = size,
                outlined = outlined(it),
                enabled = enabled(it),
                colors = colors(it),
                onClick = { onClick(it) },
                interactionSource = interactionSource,
                contentPadding = buttonContentPaddingValues,
                modifier = Modifier.weight(weight)
            )
        }
    }
}
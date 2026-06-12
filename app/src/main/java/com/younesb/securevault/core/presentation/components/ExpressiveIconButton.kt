package com.younesb.securevault.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import soup.compose.material.motion.animation.materialSharedAxisZ

/**
 * An expressive icon button component with support for loading states, rotation, and multiple sizes.
 * This button uses Material 3's expressive design system with animated icon transitions.
 *
 * Features:
 * - Animated icon transitions when icon changes
 * - Loading state with progress indicator
 * - Icon rotation support
 * - Multiple predefined sizes (extra small to extra large)
 * - Uniform or wide width options
 * - Filled or outlined variants
 *
 * @param icon The icon to display in the button.
 * @param modifier The modifier to be applied to the button.
 * @param size The size of the icon. Use IconButtonDefaults constants (e.g., extraSmallIconSize).
 * @param widthOption Width option for the button - Uniform (square) or other variants.
 * @param outlined Whether to use an outlined style instead of filled.
 * @param colors The colors to use for the button.
 * @param loading Whether to show a loading indicator instead of the icon.
 * @param enabled Whether the button is enabled for interaction.
 * @param interactionSource Optional custom interaction source for the button.
 * @param onClick Callback invoked when the button is clicked.
 *
 * @sample
 * ```
 * ExpressiveIconButton(
 *     icon = Icons.Default.Favorite,
 *     size = IconButtonDefaults.mediumIconSize,
 *     loading = isLoading,
 *     outlined = true,
 *     onClick = { /* Handle click */ }
 * )
 * ```
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveIconButton(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = IconButtonDefaults.extraSmallIconSize,
    widthOption: IconButtonDefaults.IconButtonWidthOption = IconButtonDefaults.IconButtonWidthOption.Uniform,
    outlined: Boolean = false,
    colors: IconButtonColors =
        if (outlined) IconButtonDefaults.outlinedIconButtonColors()
        else IconButtonDefaults.iconButtonColors(),
    loading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
) {
    val buttonSize = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallContainerSize(widthOption)
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallContainerSize(widthOption)
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumContainerSize(widthOption)
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largeContainerSize(widthOption)
        else -> IconButtonDefaults.extraLargeContainerSize(widthOption)
    }
    val shape = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallRoundShape
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallRoundShape
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumRoundShape
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largeRoundShape
        else -> IconButtonDefaults.extraLargeRoundShape
    }
    val pressedShape = when(size) {
        IconButtonDefaults.extraSmallIconSize -> IconButtonDefaults.extraSmallPressedShape
        IconButtonDefaults.smallIconSize -> IconButtonDefaults.smallPressedShape
        IconButtonDefaults.mediumIconSize -> IconButtonDefaults.mediumPressedShape
        IconButtonDefaults.largeIconSize -> IconButtonDefaults.largePressedShape
        else -> IconButtonDefaults.extraLargePressedShape
    }
    val content = @Composable {
        AnimatedContent(loading) { isLoading ->
            if (isLoading)
                LoadingIndicator(Modifier.size(size))
            else {
                icon()
            }
        }
    }
    if (outlined)
        OutlinedIconButton(
            onClick = onClick,
            modifier = modifier.size(buttonSize),
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(shape, pressedShape),
            colors = colors,
            interactionSource = interactionSource,
            content = content
        )
    else
        IconButton(
            onClick = onClick,
            modifier = modifier.size(buttonSize),
            enabled = enabled,
            shapes = IconButtonDefaults.shapes(shape, pressedShape),
            colors = colors,
            interactionSource = interactionSource,
            content = content,
        )
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = IconButtonDefaults.extraSmallIconSize,
    widthOption: IconButtonDefaults.IconButtonWidthOption = IconButtonDefaults.IconButtonWidthOption.Uniform,
    outlined: Boolean = false,
    colors: IconButtonColors =
        if (outlined) IconButtonDefaults.outlinedIconButtonColors()
        else IconButtonDefaults.iconButtonColors(),
    loading: Boolean = false,
    enabled: Boolean = true,
    iconRotationAngle: Float = 0f,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
) = ExpressiveIconButton(
    {
        AnimatedContent(
            icon,
            transitionSpec = {
                materialSharedAxisZ(true)
            }
        ) {
            Box {
                Icon(
                    it,
                    contentDescription = null,
                    modifier = Modifier.size(size).rotate(iconRotationAngle),
                )
            }
        }
    },
    modifier,
    size,
    widthOption,
    outlined,
    colors,
    loading,
    enabled,
    interactionSource,
    onClick
)
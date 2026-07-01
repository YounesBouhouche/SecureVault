package com.younesb.securevault.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import com.younesb.securevault.features.main.presentation.components.LoadingCircularBar

/**
 * An async image component with loading states, error handling, and fallback content.
 * Uses Coil for image loading with Material 3 styling.
 *
 * Features:
 * - Async image loading with Coil
 * - Animated loading indicator (CircularWavyProgressIndicator)
 * - Fallback icon and text when image fails to load
 * - Optional click handling
 * - Customizable shape and background
 * - Crop content scaling
 *
 * @param model The data model for the image (URL, URI, File, etc). See Coil documentation.
 * @param icon The fallback icon to display when image loading fails.
 * @param modifier The modifier to be applied to the image.
 * @param alt Optional composable content to display below the fallback icon on error.
 * @param iconTint The tint color for the fallback icon.
 * @param shape The shape to clip the image container.
 * @param background The background color visible during loading or behind transparent images.
 * @param fraction The size of the fallback icon as a fraction of the container.
 * @param onClick Optional callback invoked when the image is clicked.
 * @param onError Optional callback invoked when image loading fails.
 * @param onSuccess Optional callback invoked when image loading succeeds.
 *
 * @sample
 * ```
 * Image(
 *     model = "https://example.com/image.jpg",
 *     icon = Icons.Default.Image,
 *     modifier = Modifier.size(200.dp),
 *     alt = { Text("Failed to load") },
 *     onClick = { /* Handle click */ }
 * )
 * ```
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Image(
    model: Any?,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    alt: (@Composable () -> Unit)? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = MaterialTheme.shapes.medium,
    background: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    contentScale: ContentScale = ContentScale.Crop,
    fraction: Float = .5f,
    onClick: (() -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = "",
        modifier = modifier
            .clip(shape)
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Image) { onClick() }
                } else {
                    Modifier
                }
            ),
        contentScale = contentScale,
        loading = {
            LoadingCircularBar()
        },
        error = {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(fraction),
                        tint = iconTint
                    )
                }
                ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                    alt?.invoke()
                }
            }
        },
        onError = onError,
        onSuccess = onSuccess,
    )
}
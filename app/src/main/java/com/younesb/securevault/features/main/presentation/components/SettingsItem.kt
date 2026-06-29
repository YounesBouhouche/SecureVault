package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsItem(
    headline: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    role: Role? = null,
    supporting: String? = null,
    background: Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: Shape = MaterialTheme.shapes.large,
    onClick: () -> Unit = {},
    separator: Boolean = true,
    icon: ImageVector? = null,
    iconBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    iconTint: Color = contentColorFor(iconBackground),
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    val alpha by animateFloatAsState(if (enabled) 1f else .5f)
    Column(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    role = role,
                    onClick = onClick,
                    enabled = enabled,
                ).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        ) {
            icon?.let {
                Box(Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                supporting?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (separator and (trailingContent != null)) {
                VerticalDivider(Modifier.fillMaxHeight(.5f), 2.dp)
            }
            trailingContent?.invoke()
            if (trailingContent == null) {
                VerticalDivider(Modifier.fillMaxHeight(.5f), 2.dp)
            }
        }
        bottomContent?.invoke()
    }
}

package com.younesb.securevault.features.main.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.presentation.navigation.util.getIconAndColor
import com.younesb.securevault.features.main.presentation.util.formatFileSize

@Composable
fun DocumentItem(
    document: DocumentDto,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    selected: Boolean = false,
    onClick: () -> Unit = { },
    onSwipe: () -> Unit = { },
) {
    val (icon, color) = remember {
        document.getIconAndColor()
    }
    ListItem(
        modifier = modifier,
        shape = shape,
        selected = selected,
        leadingIcon = icon,
        iconTint = color,
        title = document.name,
        subtitle = document.size.formatFileSize(),
        onSelectedChange = {},
        onMoreClick = {},
        onClick = onClick,
        onSwipe = onSwipe,
    )
}
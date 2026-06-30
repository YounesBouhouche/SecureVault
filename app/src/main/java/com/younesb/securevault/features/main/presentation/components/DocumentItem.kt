package com.younesb.securevault.features.main.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.presentation.util.formatFileSize

@Composable
fun DocumentItem(
    document: DocumentDto,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    selected: Boolean = false,
    onClick: () -> Unit = { },
) = ListItem(
    modifier = modifier,
    shape = shape,
    selected = selected,
    leadingIcon =
        when (document.type) {
            DocumentType.NOTE -> Icons.AutoMirrored.Rounded.Note
            DocumentType.FILE,
            DocumentType.UNKNOWN -> Icons.AutoMirrored.Filled.InsertDriveFile
            DocumentType.IMAGE -> Icons.Rounded.Image
        },
    iconTint = when (document.type) {
        DocumentType.FILE -> MaterialTheme.colorScheme.secondary
        DocumentType.IMAGE -> MaterialTheme.colorScheme.error
        DocumentType.NOTE -> MaterialTheme.colorScheme.tertiary
        DocumentType.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    },
    title = document.name,
    subtitle = document.size.formatFileSize(),
    onSelectedChange = {},
    onMoreClick = {},
    onClick = onClick
)
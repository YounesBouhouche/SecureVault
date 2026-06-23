package com.younesb.securevault.features.main.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.pluralStringResource
import com.younesb.securevault.R
import com.younesb.securevault.features.main.domain.models.FolderDto

@Composable
fun FolderItem(
    folder: FolderDto,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = { },
) = ListItem(
    modifier = modifier,
    shape = shape,
    leadingIcon = Icons.Rounded.FolderOpen,
    title = folder.name,
    subtitle = pluralStringResource(R.plurals.files, folder.documents.size, folder.documents.size),
    onSelectedChange = {},
    onMoreClick = {},
    onClick = onClick
)
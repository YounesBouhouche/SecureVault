package com.younesb.securevault.features.main.presentation.navigation.routes.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.younesb.mydesignsystem.presentation.components.ExpressiveIconButton
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.presentation.components.DocumentItem
import com.younesb.securevault.features.main.presentation.components.FolderItem
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFolderClick: (String) -> Unit = { },
    onDocumentClick: (String) -> Unit = { }
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val folders by viewModel.folders.collectAsStateWithLifecycle()
    val tags by viewModel.tags.collectAsStateWithLifecycle()
    val selectedTags by viewModel.selectedTags.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    val total = folders.size + documents.size

    Column(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(bottom = 300.dp) + PaddingValues(horizontal = 8.dp) + WindowInsets.navigationBars.asPaddingValues()
        ) {
            if (documents.isNotEmpty() or folders.isNotEmpty()) {
                stickyHeader(key = "header") {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(expressiveListItemShape(0, 2))
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .padding(16.dp, 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.files),
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary
                        )
                        ExposedDropdownMenuBox(
                            expanded,
                            { expanded = it }
                        ) {
                            ExpressiveIconButton(
                                icon = Icons.Default.Tag,
                                widthOption = IconButtonDefaults.IconButtonWidthOption.Wide,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(),
                            ) {
                                expanded = true
                            }
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                containerColor = MenuDefaults.groupStandardContainerColor,
                                shape = MenuDefaults.standaloneGroupShape,
                                modifier = Modifier.width(200.dp)
                            ) {
                                val optionCount = tags.size
                                tags.forEachIndexed { index, tag ->
                                    DropdownMenuItem(
                                        shapes = MenuDefaults.itemShape(index, optionCount),
                                        text = {
                                            Text(
                                                tag.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        },
                                        selected = tag.id in selectedTags,
                                        onClick = {
                                            viewModel.toggleTagSelection(tag.id)
                                        },
                                        selectedLeadingIcon = {
                                            Icon(
                                                Icons.Filled.Check,
                                                modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                                contentDescription = null,
                                            )
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                }
                itemsIndexed(folders, { _, it -> "folder_${it.id}" }) { index, folder ->
                    FolderItem(
                        folder = folder, shape = expressiveListItemShape(
                            index + 1, total + 1, smallShape = MaterialTheme.shapes.small
                        ), modifier = Modifier.animateItem()
                    ) {
                        onFolderClick(folder.id)
                    }
                }
                itemsIndexed(documents, { _, it -> "doc_${it.id}" }) { index, document ->
                    DocumentItem(
                        document = document,
                        shape = expressiveListItemShape(
                            folders.size + index + 1,
                            total + 1,
                            smallShape = MaterialTheme.shapes.small
                        ),
                        modifier = Modifier.animateItem(),
                    ) {
                        onDocumentClick(document.id)
                    }
                }
            }
        }
    }
}
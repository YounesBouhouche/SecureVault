package com.younesb.securevault.features.main.presentation.navigation.routes.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.presentation.components.DocumentItem
import com.younesb.securevault.features.main.presentation.components.FolderItem
import com.younesb.securevault.features.main.presentation.components.listHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onFolderClick: (String) -> Unit = { },
    onDocumentClick: (String) -> Unit = { }
) {
    val viewModel = koinViewModel<BrowseViewModel>()
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val folders by viewModel.folders.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = contentPadding + PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (documents.isNotEmpty()) {
            listHeader(key = "documents_header") {
                stringResource(R.string.files)
            }
            itemsIndexed(documents, { _, it -> "doc_${it.id}" }) { index, document ->
                DocumentItem(
                    document = document,
                    shape = expressiveListItemShape(index, documents.size),
                    modifier = Modifier.animateItem()
                ) {
                    onDocumentClick(document.id)
                }
            }
        }
        if (folders.isNotEmpty()) {
            listHeader(key = "folders_header") {
                stringResource(R.string.folders)
            }
            itemsIndexed(folders, { _, it -> "folder_${it.id}" }) { index, folder ->
                FolderItem(
                    folder = folder,
                    shape = expressiveListItemShape(index, folders.size),
                    modifier = Modifier.animateItem()
                ) {
                    onFolderClick(folder.id)
                }
            }
        }
    }
}
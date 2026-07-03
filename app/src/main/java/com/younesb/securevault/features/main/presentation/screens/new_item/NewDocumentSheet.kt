package com.younesb.securevault.features.main.presentation.screens.new_item

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDocumentSheet(
    modifier: Modifier = Modifier,
    onCreateFolder: () -> Unit = { },
    onCreateTag: () -> Unit = { },
) {
    val viewModel = koinViewModel<NewDocumentViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NewDocumentScreen(uiState, modifier, viewModel::onAction, onCreateFolder, onCreateTag)
}

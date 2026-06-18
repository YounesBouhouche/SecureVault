package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DocumentScreen(
    documentId: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val viewModel = koinViewModel<DocumentViewModel> {
        parametersOf(documentId)
    }

}
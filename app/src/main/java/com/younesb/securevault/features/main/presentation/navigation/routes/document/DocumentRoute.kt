package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DocumentRoute(
    documentId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<DocumentViewModel> {
        parametersOf(documentId)
    }
    val document by viewModel.document.collectAsStateWithLifecycle()
    val file by viewModel.file.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    with(sharedTransitionScope) {
        DocumentScreen(
            document = document,
            file = file,
            uiState = uiState,
            onAction = viewModel::onAction,
            modifier = modifier.sharedBounds(
                sharedContentState = sharedTransitionScope
                    .rememberSharedContentState(key = "doc_${documentId}"),
                animatedVisibilityScope = animatedVisibilityScope,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
            )
        )
    }
}

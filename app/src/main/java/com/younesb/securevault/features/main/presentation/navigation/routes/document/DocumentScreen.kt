package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.TagDto
import com.younesb.securevault.features.main.presentation.util.Resource
import com.younesb.securevault.features.main.presentation.util.ResourcePresenter


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DocumentScreen(
    document: Resource<DocumentDto, Throwable>,
    file: Resource<Any, Throwable>,
    uiState: UiState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
) {
    ResourcePresenter(
        resource = document,
        modifier = modifier,
        idleContent = {},
    ) { doc ->
        DocumentSuccessScreen(
            document = doc,
            file = file,
            uiState = uiState,
            onAction = onAction,
            modifier = modifier,
            onBack = onBack
        )
    }
}

@Preview
@Composable
private fun DocumentScreenPreview() {
    AppTheme {
        DocumentScreen(
            document = Resource.Success(
                DocumentDto(
                    id = "1",
                    name = "Document 1",
                    createdAt = System.currentTimeMillis(),
                    size = 1024L * 1024L * 5L,
                    tags = listOf(
                        TagDto("1", "Tag 1", System.currentTimeMillis()),
                        TagDto("2", "Tag 2", System.currentTimeMillis())
                    )
                )
            ),
            file = Resource.Success(Any()),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
            uiState = UiState(isFavorite = true)
        )
    }
}
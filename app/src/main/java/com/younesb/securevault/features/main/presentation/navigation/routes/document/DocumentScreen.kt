package com.younesb.securevault.features.main.presentation.navigation.routes.document

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.ExpressiveTextField
import com.younesb.securevault.core.presentation.components.Image
import com.younesb.securevault.core.presentation.theme.AppTheme
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.domain.models.DocumentDto
import com.younesb.securevault.features.main.domain.models.DocumentType
import com.younesb.securevault.features.main.domain.models.TagDto
import com.younesb.securevault.features.main.presentation.util.Resource
import com.younesb.securevault.features.main.presentation.util.ResourcePresenter
import com.younesb.securevault.features.main.presentation.util.formatFileSize
import com.younesb.securevault.features.main.presentation.util.getOrNull
import com.younesb.securevault.features.main.presentation.util.toReadableDateString


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
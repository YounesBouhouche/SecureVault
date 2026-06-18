package com.younesb.securevault.features.main.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainSearchBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    navigateToSettings: () -> Unit = { },
) {
    val searchBarState = rememberContainedSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val expanded by remember {
        derivedStateOf {
            searchBarState.currentValue == SearchBarValue.Expanded
        }
    }
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = {
                    Text(modifier = Modifier.clearAndSetSemantics {}, text = "Search")
                },
                leadingIcon = {
                    AnimatedContent(expanded) {
                        Icon(
                            imageVector =
                                if (it) Icons.AutoMirrored.Default.ArrowBack
                                else Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { -it } + fadeIn(
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ),
        exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()) { -it } + fadeOut(
            animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
        ),
        modifier = modifier
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SearchBar(
                state = searchBarState,
                inputField = inputField,
                modifier = Modifier.weight(1f)
            )
            ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField) {
            }
            IconButton(
                modifier = Modifier
                    .size(
                        IconButtonDefaults.mediumContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Narrow
                        )
                    )
                    .height(SearchBarDefaults.InputFieldHeight),
                onClick = navigateToSettings,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shapes = IconButtonDefaults.shapes()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        }
    }
}
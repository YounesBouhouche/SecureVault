package com.younesb.securevault.features.main.presentation.navigation.routes.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.FileCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.components.IconContainer
import com.younesb.securevault.features.main.presentation.components.MainSearchBar
import com.younesb.securevault.features.main.presentation.components.header
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
context(transitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedContentScope)
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFolderClick: (String) -> Unit = { },
    onDocumentClick: (String) -> Unit = { }
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val empty by viewModel.empty.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        MainSearchBar(
            modifier = Modifier.statusBarsPadding(),
            textFieldState = viewModel.searchFieldState,
        )
        Box(
            modifier = Modifier
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().blur(if (empty) 100.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(vertical = 16.dp) + WindowInsets.navigationBars.add(
                    WindowInsets(bottom = 200.dp)
                ).asPaddingValues()
            ) {
                header(key = "pinned_header", onClick = {}) {
                    stringResource(R.string.pinned_files)
                }
                header(key = "recents_header") {
                    stringResource(R.string.recently_added)
                }
                header(key = "browse_header") {
                    stringResource(R.string.browse)
                }
            }
            if (empty) {
                Column(
                    Modifier.fillMaxSize().padding(bottom = 100.dp).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconContainer(
                        icon = Icons.Sharp.FileCopy,
                        modifier = Modifier.size(200.dp),
                        shape = MaterialShapes.PixelCircle.toShape(),
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                            alpha = 0.5f
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.no_files_placeholder),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

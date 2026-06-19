package com.younesb.securevault.features.main.presentation.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun<T, E: Throwable> ResourcePresenter(
    resource: Resource<T, E>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    transitionSpec: AnimatedContentTransitionScope<Resource<T, E>>.() -> ContentTransform = {
        fadeIn() togetherWith fadeOut()
    },
    idleContent: @Composable BoxScope.() -> Unit = {},
    errorContent: @Composable (E) -> Unit = {},
    loadingContent: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ContainedLoadingIndicator(Modifier.size(100.dp))
        }
    },
    successContent: @Composable (T) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = resource,
        transitionSpec = transitionSpec
    ) {
        Box(Modifier, contentAlignment = contentAlignment) {
            when (it) {
                is Resource.Idle -> idleContent()
                is Resource.Loading -> loadingContent()
                is Resource.Success -> successContent(it.data)
                is Resource.Error -> errorContent(it.error)
            }
        }
    }
}
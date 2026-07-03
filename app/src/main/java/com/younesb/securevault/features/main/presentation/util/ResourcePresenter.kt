package com.younesb.securevault.features.main.presentation.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.younesb.securevault.features.main.presentation.components.LoadingCircularBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun<T, E: Throwable> ResourcePresenter(
    resource: Resource<T, E>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    transitionSpec: AnimatedContentTransitionScope<Resource<T, E>>.() -> ContentTransform = {
        fadeIn() togetherWith fadeOut()
    },
    contentKey: (Resource<T, E>) -> Any? = {
        when (it) {
            is Resource.Error<*> -> "error"
            Resource.Idle -> "idle"
            Resource.Loading -> "loading"
            is Resource.Success<*> -> "success"
        }
    },
    idleContent: @Composable BoxScope.() -> Unit = {},
    errorContent: @Composable (E) -> Unit = {},
    loadingContent: @Composable BoxScope.() -> Unit = {
        LoadingCircularBar()
    },
    successContent: @Composable (T) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = resource,
        transitionSpec = transitionSpec,
        contentKey = contentKey
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
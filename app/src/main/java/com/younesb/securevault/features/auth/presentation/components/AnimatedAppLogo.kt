package com.younesb.securevault.features.auth.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedAppLogo(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
        LoadingIndicator(
            modifier = Modifier.fillMaxSize().scale(1.2f),
            color = MaterialTheme.colorScheme.onBackground,
            polygons = listOf(
                MaterialShapes.Sunny,
                MaterialShapes.VerySunny,
                MaterialShapes.Cookie12Sided,
                MaterialShapes.Cookie4Sided,
            ),
        )
        Icon(
            Icons.Default.Lock,
            null,
            modifier = Modifier.fillMaxSize(.3f),
            tint = MaterialTheme.colorScheme.background
        )
    }
}
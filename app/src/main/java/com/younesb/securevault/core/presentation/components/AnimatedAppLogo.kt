package com.younesb.securevault.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedAppLogo(modifier: Modifier = Modifier) =
    AnimatedIconContainer(Icons.Default.Lock, modifier)

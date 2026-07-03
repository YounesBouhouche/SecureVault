package com.younesb.securevault.core.presentation.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.younesb.securevault.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedAppLogo(modifier: Modifier = Modifier) =
    AnimatedIconContainer(ImageVector.vectorResource(R.drawable.app_logo), modifier)

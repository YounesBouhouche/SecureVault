package com.younesb.securevault.core.presentation.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider

class ThemeWrapper: PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        AppTheme {
            Surface(Modifier.fillMaxSize(), color = Color.Red) {
                content()
            }
        }
    }
}
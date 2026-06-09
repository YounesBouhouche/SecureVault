package com.younesb.securevault.core.presentation.theme

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.toArgb
import com.younesb.securevault.core.domain.models.preferences.Theme

@Composable
fun ComponentActivity.SetSystemBarColors(
    themeMode: Theme = Theme.SYSTEM,
) {
    val isDark =
        when (themeMode) {
            Theme.DARK -> true
            Theme.LIGHT -> false
            Theme.SYSTEM -> isSystemInDarkTheme()
        }
    val scrim = MaterialTheme.colorScheme.scrim
    DisposableEffect(isDark) {
        val statusBarStyle =
            if (isDark) {
                SystemBarStyle.dark(scrim.copy(alpha = 0f).toArgb())
            } else {
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            }
        val navigationBarStyle =
            if (isDark) {
                SystemBarStyle.dark(Color.TRANSPARENT)
            } else {
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            }
        enableEdgeToEdge(statusBarStyle = statusBarStyle, navigationBarStyle = navigationBarStyle)
        onDispose { }
    }
}

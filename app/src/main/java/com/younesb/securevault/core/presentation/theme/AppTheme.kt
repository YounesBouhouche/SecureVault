package com.younesb.securevault.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import com.younesb.securevault.core.domain.models.preferences.ColorScheme
import com.younesb.securevault.core.domain.models.preferences.Theme

@Composable
internal fun AppTheme(
    themeMode: Theme = Theme.SYSTEM,
    extraDark: Boolean = false,
    colorTheme: ColorScheme = ColorScheme.BLUE,
    dynamicColors: Boolean = true,
    primary: Color? = null,
    secondary: Color? = null,
    tertiary: Color? = null,
    content: @Composable () -> Unit,
) {
    val isDark =
        when (themeMode) {
            Theme.SYSTEM -> isSystemInDarkTheme()
            Theme.LIGHT -> false
            Theme.DARK -> true
        }
    val dynamic = dynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme =
        when {
            primary != null ->
                rememberDynamicColorScheme(
                    primary = primary,
                    secondary = secondary,
                    tertiary = tertiary,
                    isDark = isDark,
                    isAmoled = extraDark,
                )
            dynamic && isDark -> {
                dynamicDarkColorScheme(LocalContext.current)
            }
            dynamic && !isDark -> {
                dynamicLightColorScheme(LocalContext.current)
            }
            else -> colorTheme.scheme(isDark)
        }
    DynamicMaterialTheme(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        error = colorScheme.error,
        style = PaletteStyle.Expressive,
        animate = true,
        animationSpec = remember {
            MotionScheme.expressive().defaultEffectsSpec()
        },
        isDark = isDark,
        isAmoled = extraDark,
        typography = remember { AppTypography },
        content = content,
    )
}

package com.younesb.securevault.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.DynamicMaterialExpressiveTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme
import com.materialkolor.dynamiccolor.ColorSpec
import com.younesb.securevault.core.domain.models.preferences.Theme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AppTheme(
    themeMode: Theme = Theme.SYSTEM,
    extraDark: Boolean = false,
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
    val context = LocalContext.current
    val colorScheme = remember(
        primary,
        secondary,
        tertiary,
        isDark,
        extraDark,
        dynamic
    ) {
        when {
            primary != null ->
                dynamicColorScheme(
                    primary = primary,
                    secondary = secondary,
                    tertiary = tertiary,
                    isDark = isDark,
                    isAmoled = extraDark,
                )
            dynamic && isDark -> {
                dynamicDarkColorScheme(context)
            }
            dynamic && !isDark -> {
                dynamicLightColorScheme(context)
            }
            isDark -> SecureVaultDarkColorScheme
            else -> SecureVaultLightColorScheme
        }
    }
    DynamicMaterialExpressiveTheme(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        error = colorScheme.error,
        neutral = colorScheme.surface,
        neutralVariant = colorScheme.surfaceVariant,
        style = PaletteStyle.Expressive,
        specVersion = ColorSpec.SpecVersion.SPEC_2021,
        animate = true,
        motionScheme = MotionScheme.expressive(),
        animationSpec = remember {
            MotionScheme.expressive().defaultEffectsSpec()
        },
        shapes = MaterialTheme.shapes,
        isDark = isDark,
        isAmoled = extraDark,
        typography = remember { AppTypography },
        content = content,
    )
}

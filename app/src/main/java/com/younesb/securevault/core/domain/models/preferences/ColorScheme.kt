package com.younesb.securevault.core.domain.models.preferences

import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.theme.BlueColors
import com.younesb.securevault.core.presentation.theme.GreenColors
import com.younesb.securevault.core.presentation.theme.OrangeColors
import com.younesb.securevault.core.presentation.theme.PurpleColors
import com.younesb.securevault.core.presentation.theme.RedColors

enum class ColorScheme(
    val label: Int,
    val lightScheme: androidx.compose.material3.ColorScheme,
    val darkScheme: androidx.compose.material3.ColorScheme,
) {
    BLUE(R.string.blue, BlueColors.lightScheme, BlueColors.darkScheme),
    GREEN(R.string.green, GreenColors.lightScheme, GreenColors.darkScheme),
    RED(R.string.red, RedColors.lightScheme, RedColors.darkScheme),
    ORANGE(R.string.orange, OrangeColors.lightScheme, OrangeColors.darkScheme),
    PURPLE(R.string.purple, PurpleColors.lightScheme, PurpleColors.darkScheme),
    ;

    companion object {
        fun fromString(value: String): ColorScheme = entries.find { it.name.equals(value, ignoreCase = true) } ?: BLUE
    }

    fun scheme(isDark: Boolean) = if (isDark) darkScheme else lightScheme
}

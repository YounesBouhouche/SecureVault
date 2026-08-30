package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
import com.younesb.securevault.features.main.presentation.components.CheckSurface
import com.younesb.securevault.features.main.presentation.components.OptionsSheetWrapper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSheet(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { }
) {
    if (visible) {
        val viewModel = koinViewModel<ThemeViewModel>()
        val theme by viewModel.themeMode.collectAsState()
        val dynamicColors by viewModel.dynamicColors.collectAsState()
        val extraDark by viewModel.extraDark.collectAsState()
        val isDark = theme == Theme.DARK || (theme == Theme.SYSTEM && isSystemInDarkTheme())
        var tempDynamicColors by remember {
            mutableStateOf(dynamicColors)
        }
        LaunchedEffect(dynamicColors) {
            tempDynamicColors = dynamicColors
        }
        var tempExtraDark by remember {
            mutableStateOf(extraDark)
        }
        LaunchedEffect(extraDark) {
            tempExtraDark = extraDark
        }
        OptionsSheetWrapper(
            options = Theme.entries,
            option = theme,
            title = stringResource(R.string.choose_theme),
            label = { stringResource(it.label) },
            onDismissRequest = {
                it?.let {
                    viewModel.setThemeMode(it)
                    viewModel.setDynamicColors(tempDynamicColors)
                    viewModel.setExtraDark(tempExtraDark)
                }
                onDismissRequest()
            },
            modifier = modifier
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                CheckSurface(
                    checked = tempDynamicColors,
                    onCheckedChange = {
                        tempDynamicColors = it
                    },
                    text = stringResource(R.string.dynamic_colors),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            if (isDark) {
                CheckSurface(
                    checked = tempExtraDark,
                    onCheckedChange = {
                        tempExtraDark = it
                    },
                    text = stringResource(R.string.extra_dark),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}


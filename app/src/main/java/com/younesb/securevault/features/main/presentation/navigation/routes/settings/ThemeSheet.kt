package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.presentation.theme.ThemeViewModel
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
        OptionsSheetWrapper(
            options = Theme.entries,
            option = theme,
            label = { stringResource(it.label) },
            onDismissRequest = {
                it?.let {
                    viewModel.setThemeMode(it)
                }
                onDismissRequest()
            },
            modifier = modifier
        )
    }
}
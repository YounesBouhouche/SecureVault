package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.younesb.securevault.core.domain.models.preferences.Language
import com.younesb.securevault.features.main.presentation.components.OptionsSheetWrapper
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSheet(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { }
) {
    if (visible) {
        val viewModel = koinViewModel<LanguageViewModel>()
        val language by viewModel.language.collectAsState()
        OptionsSheetWrapper(
            options = Language.entries,
            option = language,
            label = { stringResource(it.label) },
            onDismissRequest = {
                it?.let {
                    viewModel.setLanguage(it)
                }
                onDismissRequest()
            },
            modifier = modifier
        )
    }
}
package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.younesb.securevault.R
import com.younesb.securevault.core.presentation.utils.GlobalEvent
import com.younesb.securevault.core.presentation.utils.GlobalEventsBus
import com.younesb.securevault.core.presentation.utils.expressiveListItemShape
import com.younesb.securevault.features.main.presentation.components.SettingsItem
import com.younesb.securevault.features.main.presentation.components.SettingsScreenWrapper
import com.younesb.securevault.features.main.presentation.components.settingsLabel
import com.younesb.securevault.features.main.presentation.navigation.MainRoutes
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onNavigate: (MainRoutes) -> Unit = { }
) {
    var themeSheetVisible by remember { mutableStateOf(false) }
    var languageSheetVisible by remember { mutableStateOf(false) }
    var resetAppDialogVisible by remember { mutableStateOf(false) }
    val viewModel = koinViewModel<SettingsViewModel>()
    val scope = rememberCoroutineScope()
    SettingsScreenWrapper(
        icon = Icons.Default.Settings,
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        settingsLabel {
            stringResource(R.string.general)
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.theme),
                supporting = stringResource(R.string.choose_your_preferred_theme),
                shape = expressiveListItemShape(0, 2),
                icon = Icons.Default.Palette,
                onClick = { themeSheetVisible = true }
            )
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.language),
                supporting = stringResource(R.string.choose_your_preferred_language),
                shape = expressiveListItemShape(1, 2),
                icon = Icons.Default.Language,
                onClick = { languageSheetVisible = true }
            )
        }
        settingsLabel {
            stringResource(R.string.security)
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.change_password),
                supporting = stringResource(R.string.update_your_lock_pin_password),
                shape = expressiveListItemShape(0, 2),
                icon = Icons.Default.Password,
                iconBackground = MaterialTheme.colorScheme.errorContainer,
                onClick = { onNavigate(MainRoutes.ChangePassword) }
            )
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.reset_app),
                supporting = stringResource(R.string.restore_the_app_to_its_default_settings),
                shape = expressiveListItemShape(1, 2),
                icon = Icons.Default.Restore,
                iconBackground = MaterialTheme.colorScheme.errorContainer,
                onClick = { resetAppDialogVisible = true }
            )
        }
        settingsLabel {
            stringResource(R.string.more)
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.about),
                supporting = stringResource(R.string.learn_more_about_the_app),
                shape = expressiveListItemShape(0, 2),
                icon = Icons.Default.Info,
                iconBackground = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = { onNavigate(MainRoutes.About) }
            )
        }
        item {
            SettingsItem(
                headline = stringResource(R.string.more_apps),
                supporting = stringResource(R.string.discover_other_apps_by_the_developer),
                shape = expressiveListItemShape(1, 2),
                icon = Icons.Default.Apps,
                iconBackground = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = {
                    scope.launch {
                        GlobalEventsBus.sendEvent(GlobalEvent.LaunchExternalLink("https://github.com/YounesBouhouche/"))
                    }
                }
            )
        }
    }
    ThemeSheet(
        visible = themeSheetVisible,
        onDismissRequest = { themeSheetVisible = false }
    )
    LanguageSheet(
        visible = languageSheetVisible,
        onDismissRequest = { languageSheetVisible = false }
    )
    ResetAppDialog(
        visible = resetAppDialogVisible,
        onDismissRequest = { resetAppDialogVisible = false },
        onConfirm = {
            viewModel.resetApp()
        }
    )
}
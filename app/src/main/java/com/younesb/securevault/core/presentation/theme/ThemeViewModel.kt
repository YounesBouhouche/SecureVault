package com.younesb.securevault.core.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.domain.models.preferences.ColorScheme
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    val repository: PreferencesRepository
): ViewModel() {
    val themeMode = SettingsPreference.ThemeMode.getState()
    val extraDark = SettingsPreference.ExtraDarkMode.getState()
    val colorScheme = SettingsPreference.ColorSchemeMode.getState()
    val dynamicColors = SettingsPreference.DynamicColor.getState()

    internal fun<T, R> SettingsPreference<T, R>.getState() =
        repository.get(this).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            defaultValue
        )

    internal fun <T, R> SettingsPreference<T, R>.set(value: R) {
        viewModelScope.launch {
            repository.set(this@set, value)
        }
    }

    fun setThemeMode(value: Theme) {
        SettingsPreference.ThemeMode.set(value)
    }

    fun setDynamicColors(value: Boolean) {
        SettingsPreference.DynamicColor.set(value)
    }

    fun setExtraDark(value: Boolean) {
        SettingsPreference.ExtraDarkMode.set(value)
    }

    fun setColorScheme(value: ColorScheme) {
        SettingsPreference.ColorSchemeMode.set(value)
    }
}
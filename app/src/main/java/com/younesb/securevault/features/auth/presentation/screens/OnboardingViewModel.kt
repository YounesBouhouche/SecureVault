package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class OnboardingViewModel(
    val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _theme = preferencesRepository.get(SettingsPreference.ThemeMode)
    val theme = _theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SettingsPreference.ThemeMode.defaultValue
    )

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            preferencesRepository.set(SettingsPreference.ThemeMode, theme)
        }
    }
}
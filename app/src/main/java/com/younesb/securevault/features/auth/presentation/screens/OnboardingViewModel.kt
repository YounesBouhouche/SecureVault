package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import com.younesb.securevault.features.main.presentation.navigation.MainRoutes
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    val preferencesRepository: PreferencesRepository,
    val authRepository: AuthRepository,
) : ViewModel() {
    private val _theme = preferencesRepository.get(SettingsPreference.ThemeMode)
    val theme = _theme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SettingsPreference.ThemeMode.defaultValue
    )
    val authState = authRepository.observeAuthState()

    init {
        viewModelScope.launch {
            launch {
                val state = authRepository.checkAuthState()
                println("Auth state: $state")
                when(state) {
                    AuthManager.AuthState.RequiresBiometric ->
                        authRepository.authenticate(null)
                    else -> Unit
                }
            }
            launch {
                authState.collectLatest {
                    println("Auth state changed: $it")
                    if (it is AuthManager.AuthState.Authenticated) {
                        EventBus.sendEvent(Event.Navigate(NavRoutes.Main))
                    }
                }
            }
        }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            preferencesRepository.set(SettingsPreference.ThemeMode, theme)
        }
    }
}
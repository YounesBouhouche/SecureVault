package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.datastore.SettingsPreference
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.models.preferences.Theme
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.core.domain.repositories.PreferencesRepository
import com.younesb.securevault.core.util.Task
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.features.auth.presentation.util.AuthEventsBus
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
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

    private val _remainingTime = MutableStateFlow(0L)
    val remainingTime = _remainingTime.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0L
    )

    private val task = Task()

    private val _authState = authRepository.observeAuthState().onEach { state ->
        if (state is AuthManager.AuthState.AttemptsExceeded) {
            task.stop()
            task.startRepeating(1000) {
                _remainingTime.value = ((state.timeOut - System.currentTimeMillis()) / 1000).also {
                    if (it < 0) {
                        authRepository.checkAuthState()
                        task.stop()
                    }
                }.coerceIn(0, null)
            }
        } else {
            task.stop()
            _remainingTime.value = 0L
        }
    }
    val authState = _authState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    init {
        viewModelScope.launch {
            val state = authRepository.checkAuthState()
            when(state) {
                AuthManager.AuthState.RequiresBiometric -> authenticateWithBiometrics()
                AuthManager.AuthState.Authenticated -> AuthEventsBus.sendEvent(AuthEvent.Navigate(NavRoutes.Main))
                else -> Unit
            }
        }
    }

    fun authenticateWithBiometrics() {
        viewModelScope.launch {
            if (authRepository.authenticate(null))
                AuthEventsBus.sendEvent(AuthEvent.Navigate(NavRoutes.Main))
            else if (_authState.first() is AuthManager.AuthState.RequiresPin)
                AuthEventsBus.sendEvent(AuthEvent.AuthNavigate(AuthRoutes.EnterPin))
        }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            preferencesRepository.set(SettingsPreference.ThemeMode, theme)
        }
    }
}
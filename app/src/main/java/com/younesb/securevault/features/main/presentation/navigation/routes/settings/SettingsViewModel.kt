package com.younesb.securevault.features.main.presentation.navigation.routes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.utils.onSuccess
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.features.auth.presentation.util.AuthEventsBus
import com.younesb.securevault.features.main.domain.usecases.ResetAppUseCase
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.launch

class SettingsViewModel(
    val resetAppUseCase: ResetAppUseCase
): ViewModel() {
    fun resetApp() {
        viewModelScope.launch {
            resetAppUseCase().onSuccess {
                AuthEventsBus.sendEvent(AuthEvent.Navigate(NavRoutes.Auth))
            }
        }
    }
}
package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SetupViewModel(
    biometricPromptManager: BiometricPromptManager
) : ViewModel() {
    val promptResult = biometricPromptManager.promptResults.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    init {
        viewModelScope.launch {
            promptResult.collectLatest {
                checkSuccessAndNavigate(it)
            }
        }
    }

    fun showBiometricPrompt() {
        when {
            checkSuccessAndNavigate(promptResult.value) -> return
            promptResult.value is BiometricPromptManager.BiometricResult.AuthenticationNotSet ->
                sendEvent(Event.LaunchScreenLockSettings)
            else -> sendEvent(
                Event.ShowBiometricPrompt(
                    title = "Biometric Authentication",
                    description = "Please authenticate to set up biometric authentication for your vault.",
                )
            )
        }
    }

    private fun checkSuccessAndNavigate(result: BiometricPromptManager.BiometricResult?): Boolean {
        return if (result is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
            sendEvent(Event.Navigate(AuthRoutes.SetupPin))
            true
        }
        else false
    }

    fun skip() {
        sendEvent(Event.Navigate(AuthRoutes.SetupPin))
    }

    internal fun sendEvent(event: Event) {
        viewModelScope.launch {
            EventBus.sendEvent(event)
        }
    }
}
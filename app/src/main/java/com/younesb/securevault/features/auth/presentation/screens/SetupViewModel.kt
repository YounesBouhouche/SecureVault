package com.younesb.securevault.features.auth.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.core.presentation.events.EventBus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SetupViewModel(
    val authRepository: AuthRepository,
    biometricPromptManager: BiometricPromptManager
) : ViewModel() {
    val resultChannel = biometricPromptManager.resultChannel
    val promptResult = biometricPromptManager.promptResults.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    fun showBiometricPrompt() {
        println("Checking biometric prompt result: ${promptResult.value}")
        when {
            checkSuccessAndNavigate(promptResult.value) -> return
            promptResult.value is BiometricPromptManager.BiometricResult.AuthenticationNotSet ->
                sendEvent(AuthEvent.LaunchScreenLockSettings)
            else -> sendEvent(
                AuthEvent.ShowBiometricPrompt(
                    title = "Biometric Authentication",
                    description = "Please authenticate to set up biometric authentication for your vault.",
                )
            )
        }
        viewModelScope.launch {
            checkSuccessAndNavigate(resultChannel.first())
        }
    }

    private fun checkSuccessAndNavigate(result: BiometricPromptManager.BiometricResult?): Boolean {
        return if (result is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
            authRepository.updateSetupCredentialsBiometrics(true)
            sendEvent(AuthEvent.AuthNavigate(AuthRoutes.SetupPin))
            true
        }
        else false
    }

    fun skip() {
        sendEvent(AuthEvent.AuthNavigate(AuthRoutes.SetupPin))
    }

    internal fun sendEvent(event: AuthEvent) {
        viewModelScope.launch {
            EventBus.sendEvent(event)
        }
    }
}
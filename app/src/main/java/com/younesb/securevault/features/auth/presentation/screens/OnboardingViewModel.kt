package com.younesb.securevault.features.auth.presentation.screens

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.repositories.AuthRepository
import com.younesb.securevault.core.util.Task
import com.younesb.securevault.features.auth.presentation.components.DragValue
import com.younesb.securevault.features.auth.presentation.navigation.AuthRoutes
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.features.auth.presentation.util.AuthEventsBus
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.navigation.NavRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    val authRepository: AuthRepository,
    biometricPromptManager: BiometricPromptManager
) : ViewModel() {
    val draggableState = AnchoredDraggableState(initialValue = DragValue.Start)
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
            CoroutineScope(AndroidUiDispatcher.Main).launch {
                draggableState.animateTo(DragValue.Start)
            }
        }
    }


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
            CoroutineScope(AndroidUiDispatcher.Main).launch {
                draggableState.animateTo(DragValue.Start)
            }
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

    fun setup() {
        when (authState.value) {
            is AuthManager.AuthState.RequiresBiometric -> authenticateWithBiometrics()
            is AuthManager.AuthState.NoCredentials -> showBiometricPrompt()
            is AuthManager.AuthState.RequiresPin -> sendEvent(AuthEvent.AuthNavigate(AuthRoutes.EnterPin))
            else -> {}
        }
    }

    internal fun sendEvent(event: AuthEvent) {
        viewModelScope.launch {
            AuthEventsBus.sendEvent(event)
        }
    }
}
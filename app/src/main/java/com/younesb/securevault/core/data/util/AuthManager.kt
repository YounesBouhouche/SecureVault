package com.younesb.securevault.core.data.util

import com.younesb.securevault.core.data.datastore.CredentialsDataStore
import com.younesb.securevault.core.data.models.Credentials
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.Event
import com.younesb.securevault.features.auth.presentation.util.EventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class AuthManager(
    val dataStore: CredentialsDataStore,
    biometricManager: BiometricPromptManager
) {
    private var _state: MutableStateFlow<AuthState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
    private var _attempts = 0
    private val resultChannel = biometricManager.resultChannel

    sealed interface AuthState {
        data object NoCredentials : AuthState
        data object RequiresBiometric : AuthState
        data object RequiresPin : AuthState
        data object Authenticated : AuthState
        data object LockedOut : AuthState

        data object AttemptsExceeded : AuthState
    }

    suspend fun checkAuthState(): AuthState? {
        println("Credentials: ${dataStore.getCredentials()}")
        if (_state.value != AuthState.Authenticated)
            _state.value =
                when {
                    dataStore.getCredentials() == null -> AuthState.NoCredentials
                    dataStore.getCredentials()?.biometricEnabled == true -> AuthState.RequiresBiometric
                    else -> AuthState.RequiresPin
                }
        return _state.value
    }

    suspend fun authenticate(inputPin: String?): Boolean {
        if (_state.value == null) checkAuthState()
        when (_state.value) {
            AuthState.Authenticated -> return true
            AuthState.AttemptsExceeded,
            AuthState.LockedOut,
            AuthState.NoCredentials -> return false

            else -> {
                val biometricsPassed = if (_state.value is AuthState.RequiresBiometric) {
                    EventBus.sendEvent(Event.ShowBiometricPrompt("SecureVault", "Biometrics required"))
                    val result = resultChannel.first() is
                            BiometricPromptManager.BiometricResult.AuthenticationSuccess
                    println("Biometric result: $result")
                    if (result) {
                        _state.value = AuthState.Authenticated
                        return true
                    }
                    _state.value = AuthState.RequiresPin
                    false
                } else true
                if (inputPin == null) return false
                val passed = dataStore.authenticate(inputPin, biometricsPassed)
                return if (passed) {
                    _state.value = AuthState.Authenticated
                    true
                } else  {
                    _attempts++
                    if (_attempts >= MAX_ATTEMPTS) {
                        _state.value = AuthState.LockedOut
                    }
                    false
                }
            }
        }
    }

    suspend fun updateCredentials(biometricEnabled: Boolean, pin: String): Boolean {
        return when(state.value) {
            AuthState.Authenticated, AuthState.NoCredentials -> {
                if (state.value == null) _state.value = AuthState.Authenticated
                dataStore.updateCredentials(Credentials(biometricEnabled, pin))
                true
            }
            else -> false
        }
    }

    fun lock() {
        _state.value = AuthState.LockedOut
    }

    companion object {
        const val MAX_ATTEMPTS = 5
    }
}
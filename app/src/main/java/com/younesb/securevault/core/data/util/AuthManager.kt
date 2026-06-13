package com.younesb.securevault.core.data.util

import com.younesb.securevault.core.data.datastore.AuthAttemptsDataStore
import com.younesb.securevault.core.data.datastore.CredentialsDataStore
import com.younesb.securevault.core.data.models.Credentials
import com.younesb.securevault.features.auth.presentation.util.BiometricPromptManager
import com.younesb.securevault.features.auth.presentation.util.AuthEvent
import com.younesb.securevault.core.presentation.events.EventBus
import com.younesb.securevault.features.auth.presentation.util.AuthEventsBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class AuthManager(
    val credentialsDataStore: CredentialsDataStore,
    val authAttemptsDataStore: AuthAttemptsDataStore,
    biometricManager: BiometricPromptManager
) {
    private var _state: MutableStateFlow<AuthState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
    private var _attempts = 0
    private val resultChannel = biometricManager.resultChannel

    sealed interface AuthState {
        data object NoCredentials : AuthState
        data object RequiresBiometric : AuthState
        data class RequiresPin(val remainingAttempts: Int) : AuthState
        data object Authenticated : AuthState
        data object LockedOut : AuthState

        data class AttemptsExceeded(val timeOut: Long): AuthState
    }

    suspend fun checkAuthState(): AuthState? {
        authAttemptsDataStore.safeResetAttempts()
            _state.value =
                when {
                    authAttemptsDataStore.isLocked() ->
                        AuthState.AttemptsExceeded(authAttemptsDataStore.getLockTimeOut())
                    credentialsDataStore.getCredentials() == null -> AuthState.NoCredentials
                    credentialsDataStore.getCredentials()?.biometricEnabled == true ->
                        AuthState.RequiresBiometric
                    else -> AuthState.RequiresPin(MAX_ATTEMPTS - _attempts)
                }
        return _state.value
    }

    suspend fun authenticate(inputPin: String?): Boolean {
        if (_state.value == null) checkAuthState()
        return when (_state.value) {
            AuthState.Authenticated -> true
            is AuthState.AttemptsExceeded -> false
            AuthState.RequiresBiometric -> {
                AuthEventsBus.sendEvent(AuthEvent.ShowBiometricPrompt("SecureVault", "Biometrics required"))
                val result = resultChannel.first() is
                        BiometricPromptManager.BiometricResult.AuthenticationSuccess
                _state.value =
                    if (result) {
                        authAttemptsDataStore.updateState(0)
                        AuthState.Authenticated
                    }
                    else AuthState.RequiresPin(MAX_ATTEMPTS - _attempts)
                result
            }
            is AuthState.RequiresPin -> {
                if (inputPin == null) return false
                if (credentialsDataStore.authenticate(inputPin, false)) {
                    _state.value = AuthState.Authenticated
                    authAttemptsDataStore.updateState(0)
                    true
                } else  {
                    _attempts++
                    authAttemptsDataStore.updateState(_attempts)
                    _state.value = if (authAttemptsDataStore.isLocked()) {
                        AuthState.AttemptsExceeded(authAttemptsDataStore.getLockTimeOut())
                    } else {
                        AuthState.RequiresPin(MAX_ATTEMPTS - _attempts)
                    }
                    false
                }
            }
            else -> false
        }
    }

    suspend fun updateCredentials(biometricEnabled: Boolean, pin: String): Boolean {
        return when(state.value) {
            AuthState.Authenticated, AuthState.NoCredentials -> {
                if (state.value == null) _state.value = AuthState.Authenticated
                credentialsDataStore.updateCredentials(Credentials(biometricEnabled, pin))
                true
            }
            else -> false
        }
    }

    fun lock() {
        _state.value = AuthState.LockedOut
    }

    suspend fun unlock() {
        _state.value = null
        checkAuthState()
    }

    companion object {
        const val MAX_ATTEMPTS = 5
    }
}
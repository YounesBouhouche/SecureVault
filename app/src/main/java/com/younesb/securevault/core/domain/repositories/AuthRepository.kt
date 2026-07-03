package com.younesb.securevault.core.domain.repositories

import com.younesb.securevault.core.data.util.AuthManager.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun checkAuthState(): AuthState?
    fun observeAuthState(): StateFlow<AuthState?>
    fun updateSetupCredentialsBiometrics(biometricEnabled: Boolean)
    fun updateSetupCredentialsPin(pin: String)
    suspend fun saveCredentials(): Boolean
    suspend fun authenticate(inputPin: String?): Boolean
    suspend fun resetCredentials()
}
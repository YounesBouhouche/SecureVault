package com.younesb.securevault.core.data.repositories

import com.younesb.securevault.core.data.util.AuthManager
import com.younesb.securevault.core.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    val authManager: AuthManager,
): AuthRepository {
    private var newBiometricEnabled = false
    private var newPin: String = ""

    override suspend fun checkAuthState() = authManager.checkAuthState()

    override fun observeAuthState() = authManager.state

    override fun updateSetupCredentialsBiometrics(biometricEnabled: Boolean) {
        newBiometricEnabled = biometricEnabled
    }

    override fun updateSetupCredentialsPin(pin: String) {
        newPin = pin
    }

    override suspend fun authenticate(inputPin: String?) = authManager.authenticate(inputPin)
    override suspend fun resetCredentials() {
        authManager.resetCredentials()
    }

    override suspend fun saveCredentials(): Boolean = authManager.updateCredentials(newBiometricEnabled, newPin)
}
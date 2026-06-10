package com.younesb.securevault.core.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.younesb.securevault.core.data.models.Credentials
import com.younesb.securevault.core.util.crypto.Crypto
import com.younesb.securevault.core.util.crypto.CredentialsCryptoSerializer
import com.younesb.securevault.core.util.crypto.Signer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class CredentialsDataStore(
    private val context: Context,
    crypto: Crypto
) {
    private val serializer = CredentialsCryptoSerializer(null, crypto)
    private val Context.credentialsDataStore by dataStore(
        fileName = "credentials",
        serializer = serializer
    )

    suspend fun updateCredentials(credentials: Credentials) {
        context.credentialsDataStore.updateData { credentials }
    }

    suspend fun authenticate(inputPin: String, biometricsPassed: Boolean): Boolean {
        val credentials = context.credentialsDataStore.data.first() ?: return false
        println("Credentials: $credentials")
        if (credentials.biometricEnabled and biometricsPassed) return true
        val pinMatches = Signer.verifyPassword(inputPin, credentials.pin)
        return pinMatches
    }

    suspend fun clearCredentials() {
        context.credentialsDataStore.updateData { null }
    }

    suspend fun getCredentials(): Credentials? {
        return context.credentialsDataStore.data.firstOrNull()
    }
}
package com.younesb.securevault.core.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.younesb.securevault.core.data.models.Credentials
import com.younesb.securevault.core.util.crypto.Crypto
import com.younesb.securevault.core.util.crypto.CryptoSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class CredentialsDataStore(
    private val context: Context,
    crypto: Crypto
) {
    private val serializer = CryptoSerializer(null, crypto)
    private val Context.credentialsDataStore by dataStore(
        fileName = "credentials",
        serializer = serializer
    )

    suspend fun updateCredentials(credentials: Credentials) {
        println("Updating credentials: $credentials")
        context.credentialsDataStore.updateData { credentials }
    }

    suspend fun authenticate(inputPin: String, biometricsPassed: Boolean): Boolean {
        val credentials = context.credentialsDataStore.data.first() ?: return false
        if (credentials.biometricEnabled) return biometricsPassed
        val pinMatches = credentials.pin == inputPin
        return pinMatches
    }

    suspend fun clearCredentials() {
        context.credentialsDataStore.updateData { Credentials() }
    }

    suspend fun getCredentials(): Credentials? {
        return context.credentialsDataStore.data.firstOrNull()
    }
}
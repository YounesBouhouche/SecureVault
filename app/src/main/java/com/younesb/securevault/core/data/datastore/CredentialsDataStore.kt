package com.younesb.securevault.core.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.younesb.securevault.core.data.models.Credentials
import com.younesb.securevault.core.util.crypto.Crypto
import com.younesb.securevault.core.util.crypto.CredentialsCryptoSerializer
import com.younesb.securevault.core.util.crypto.Signer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

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
        withContext(Dispatchers.IO) {
            context.credentialsDataStore.updateData { credentials }
        }
    }

    suspend fun authenticate(inputPin: String, biometricsPassed: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            val credentials = context.credentialsDataStore.data.first() ?: return@withContext false
            if (credentials.biometricEnabled and biometricsPassed) return@withContext true
            val pinMatches = Signer.verifyPassword(inputPin, credentials.pin)
            pinMatches
        }
    }

    suspend fun clearCredentials() {
        withContext(Dispatchers.IO) {
            context.credentialsDataStore.updateData { null }
        }
    }

    suspend fun getCredentials(): Credentials? = withContext(Dispatchers.IO) {
        context.credentialsDataStore.data.firstOrNull()
    }
}
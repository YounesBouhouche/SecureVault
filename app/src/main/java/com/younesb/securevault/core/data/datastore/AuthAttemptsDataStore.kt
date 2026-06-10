package com.younesb.securevault.core.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.younesb.securevault.core.data.models.AttemptsState
import com.younesb.securevault.core.util.crypto.AttemptsCryptoSerializer
import com.younesb.securevault.core.util.crypto.Crypto
import kotlinx.coroutines.flow.first

class AuthAttemptsDataStore(
    private val context: Context,
    crypto: Crypto
) {
    private val serializer = AttemptsCryptoSerializer(AttemptsState(), crypto)
    private val Context.authStateDataStore by dataStore(
        fileName = "auth_state",
        serializer = serializer
    )

    suspend fun updatePreferences(max: Int, durationMins: Int) {
        context.authStateDataStore.updateData {
            it.copy(maxAttempts = max, durationMins = durationMins)
        }
    }

    suspend fun updateState(attempts: Int, time: Long = System.currentTimeMillis()) {
        context.authStateDataStore.updateData { it.copy(
            attempts = attempts,
            lastAttemptTime = time
        ) }
    }

    suspend fun isLocked() = context.authStateDataStore.data.first().let {
        val timeSinceLastAttempt = System.currentTimeMillis() - it.lastAttemptTime
        it.attempts >= it.maxAttempts && timeSinceLastAttempt < it.durationMins * 60 * 1000
    }

    suspend fun getLockTimeOut() = context.authStateDataStore.data.first().let {
        it.lastAttemptTime + it.durationMins * 60 * 1000
    }

    suspend fun safeResetAttempts() {
        context.authStateDataStore.data.first().let {
            val timeSinceLastAttempt = System.currentTimeMillis() - it.lastAttemptTime
            println("Time since last attempt: $timeSinceLastAttempt ms")
            println("Duration: ${it.durationMins * 60 * 1000}")
            if (timeSinceLastAttempt >= it.durationMins * 60 * 1000)
                context.authStateDataStore.updateData { state -> state.copy(attempts = 0) }
        }
    }

    fun observeAttemptsState() = context.authStateDataStore.data
}
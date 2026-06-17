package com.younesb.securevault.core.util.crypto

import androidx.datastore.core.Serializer
import com.younesb.securevault.core.data.models.AttemptsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64


class AttemptsCryptoSerializer(
    override val defaultValue: AttemptsState,
    val crypto: Crypto
): Serializer<AttemptsState> {
    override suspend fun readFrom(input: InputStream): AttemptsState {
        val cipherBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val cipherDecoded = Base64.getDecoder().decode(cipherBytes)
        val plainBytes = try {
            crypto.decrypt(cipherDecoded)
        } catch (_: Exception) {
            return defaultValue
        }
        val plain = plainBytes.decodeToString()
        return Json.decodeFromString(plain)
    }

    override suspend fun writeTo(t: AttemptsState, output: OutputStream) {
        val plain = Json.encodeToString(t)
        val plainBytes = plain.toByteArray()
        val cipher = crypto.encrypt(plainBytes)
        val cipherBase64 = Base64.getEncoder().encode(cipher)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(cipherBase64)
            }
        }
    }
}
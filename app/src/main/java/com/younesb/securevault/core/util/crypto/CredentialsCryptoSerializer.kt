package com.younesb.securevault.core.util.crypto

import androidx.datastore.core.Serializer
import com.younesb.securevault.core.data.models.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64


class CredentialsCryptoSerializer(
    override val defaultValue: Credentials?,
    val crypto: Crypto
): Serializer<Credentials?> {
    override suspend fun readFrom(input: InputStream): Credentials? {
        val cipherBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val cipherDecoded = Base64.getDecoder().decode(cipherBytes)
        val plainBytes = crypto.decrypt(cipherDecoded)
        val plain = plainBytes.decodeToString()
        return Json.decodeFromString(plain)
    }

    override suspend fun writeTo(t: Credentials?, output: OutputStream) {
        val plain = Json.encodeToString(t?.let {
            it.copy(pin = withContext(Dispatchers.Default) {
                Signer.hashString(it.pin)
            })
        })
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
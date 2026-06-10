package com.younesb.securevault.core.util.crypto

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


object Signer {
    fun generateSalt(): ByteArray {
        val sr = SecureRandom()
        val salt = ByteArray(16)
        sr.nextBytes(salt)
        return salt
    }

    fun hashString(original: String, salt: ByteArray = generateSalt()): String {
        val keySpec = PBEKeySpec(original.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hash = factory.generateSecret(keySpec).encoded
        return Base64.encodeToString(salt + hash, Base64.NO_WRAP)
    }

    fun verifyPassword(input: String, hash: String): Boolean {
        val combinedData = Base64.decode(hash, Base64.NO_WRAP)
        val salt = combinedData.copyOfRange(0, 16)
        val newHash = hashString(input, salt)
        println("Input: $input, Hash: $hash, New Hash: $newHash")
        return hash == newHash
    }

    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 60000
    private const val KEY_LENGTH = 256
}
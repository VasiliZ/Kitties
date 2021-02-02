package com.github.rtyvZ.kitties.common.cryptography

import android.util.Base64
import android.util.Log
import com.github.rtyvZ.kitties.common.Strings
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Encryptor<T, V> {
    fun encryptData(data: T): Pair<V, String>? {
        runCatching {
            if (data != null) {
                val secretKey = generateSecretKey()
                secretKey?.let {
                    val encryptData = encrypt(data, secretKey)
                     val key = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
                    return Pair(encryptData, key)
                }
            }
        }.onFailure {
            Log.d(this.javaClass.canonicalName, it.message.toString())
        }

        return null
    }

    private fun encrypt(data: T, secretKey: SecretKey): V {
        val cipher = Cipher.getInstance(Strings.Crypt.TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(ByteArray(16)))
        return when (data) {
            is String -> {
                val encryptValue =
                    cipher.doFinal(data.toByteArray(charset(Strings.Charsets.UTF_8)))
                Base64.encodeToString(encryptValue, Base64.DEFAULT) as V
            }
            else -> throw IllegalArgumentException("wrong type")
        }
    }

    private fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance(Strings.Crypt.AES_ALGORITHM)
        keyGenerator.init(256, secureRandom)
        return keyGenerator.generateKey()
    }

}

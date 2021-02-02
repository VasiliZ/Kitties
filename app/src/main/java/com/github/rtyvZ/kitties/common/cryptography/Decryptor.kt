package com.github.rtyvZ.kitties.common.cryptography

import android.util.Base64
import android.util.Log
import com.github.rtyvZ.kitties.common.Strings
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Decryptor<T, V> {

    private fun decrypt(encryptedToken: T, secretKey: SecretKeySpec): String {
        val cipher = Cipher.getInstance(Strings.Crypt.TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ByteArray(16)))
        return when (encryptedToken) {
            is String -> {
                val decodedValue = Base64.decode(encryptedToken, Base64.DEFAULT)
                val decValue = cipher.doFinal(decodedValue)
                String(decValue)
            }
            else -> {
                throw IllegalArgumentException("Unknown type")
            }
        }
    }

    fun decryptData(data: T?, keyForDecrypt: String?): V? {
        runCatching {
            var result: String? = null

            if (data != null) {
                val decodedKey = Base64.decode(
                    keyForDecrypt?.toByteArray(charset(Strings.Charsets.UTF_8)),
                    Base64.DEFAULT
                )
                val restoredKey = SecretKeySpec(decodedKey, Strings.Crypt.AES_ALGORITHM)
                result = decrypt(data, restoredKey)
            }

            return result as V
        }.onFailure {
            Log.d(this.javaClass.canonicalName, it.message.toString())
        }

        return null
    }
}

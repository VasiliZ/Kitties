package com.github.rtyvZ.kitties.common.cryptography

class Cryptographer<T,V> {

    fun crypt(data:T):Pair<V, String>?{
        return Encryptor<T, V>().encryptData(data)
    }

    fun decrypt(data: T, keyForDecrypt:String):V?{
        return Decryptor<T,V>().decryptData(data,keyForDecrypt)
    }
}
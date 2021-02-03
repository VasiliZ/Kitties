package com.github.rtyvZ.kitties.common

interface PreferenceHelper {
    fun getUserId(): String?
    fun getEncryptKey(): String?
    fun getSecretKey(): String?
    fun saveUserId(userId: String)
    fun saveEncryptKey(encrKey: String)
    fun saveSecretKey(secretKey: String)
}
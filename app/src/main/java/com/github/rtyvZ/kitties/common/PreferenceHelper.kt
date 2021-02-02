package com.github.rtyvZ.kitties.common

interface PreferenceHelper {
    fun getUserId(): String?
    fun getEncryptKey(): String?
    fun getSecretKey(): String?
    fun saveUserId(userId: String)
    fun saveEncryptKey(userId: String)
    fun saveSecretKey(userId: String)
}
package com.github.rtyvZ.kitties.common

interface PreferenceHelper {
    fun getUserId(): String?
    fun saveUserId(userId: String)
}
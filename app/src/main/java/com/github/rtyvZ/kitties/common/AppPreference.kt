package com.github.rtyvZ.kitties.common

import android.content.Context
import javax.inject.Inject

class AppPreference @Inject constructor(
    context: Context,
    @PreferenceInfo val preferenceFileName: String
) : PreferenceHelper {

    private val prefs = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)

    override fun getUserId() = prefs.getString(SESSION_USER_ID, null)
    override fun getEncryptKey() = prefs.getString(ENCRYPT_KEY_FOR_KITTY_APP, null)

    override fun getSecretKey() = prefs.getString(SECRET_KEY_ID, null)

    override fun saveUserId(userId: String) {
        prefs.edit()
            .putString(SESSION_USER_ID, userId)
            .apply()
    }

    override fun saveEncryptKey(key: String) {
        prefs.edit()
            .putString(ENCRYPT_KEY_FOR_KITTY_APP, key)
            .apply()
    }

    override fun saveSecretKey(key: String) {
        prefs.edit()
            .putString(SECRET_KEY_ID, key)
            .apply()
    }

    companion object {
        private const val SESSION_USER_ID = "SESSION_USER_ID"
        private const val SECRET_KEY_ID = "SECRET_KEY_FOR_KITTY_APP"
        private const val ENCRYPT_KEY_FOR_KITTY_APP = "ENCRYPT_KEY_FOR_KITTY_APP"
    }
}
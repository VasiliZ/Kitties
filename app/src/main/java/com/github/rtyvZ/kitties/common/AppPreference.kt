package com.github.rtyvZ.kitties.common

import android.content.Context
import javax.inject.Inject

class AppPreference @Inject constructor(
    context: Context,
    @PreferenceInfo val preferenceFileName: String
) : PreferenceHelper {

    private val prefs = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
    override fun getUserId() = prefs.getString(SESSION_USER_ID, null)

    override fun saveUserId(userId: String) {
        prefs.edit()
            .putString(SESSION_USER_ID, userId)
            .apply()
    }

    companion object {
        private const val SESSION_USER_ID = "SESSION_USER_ID"
    }
}
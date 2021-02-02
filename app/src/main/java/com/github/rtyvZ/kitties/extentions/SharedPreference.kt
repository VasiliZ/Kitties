package com.github.rtyvZ.kitties.extentions

import android.content.SharedPreferences

private const val SESSION_USER_ID = "SESSION_USER_ID"


fun SharedPreferences.getUserId() = this.getString(SESSION_USER_ID, null)



fun SharedPreferences.saveUserId(userId: String) {
    this.edit()
        .putString(SESSION_USER_ID, userId)
        .apply()
}



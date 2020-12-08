package com.github.rtyvZ.kitties.common

import com.github.rtyvZ.kitties.common.models.UserSession
import javax.inject.Inject

class SessionStorage @Inject constructor(private val prefs: AppPreference) :
    UserInternalStorageContract {
    private var userSession: UserSession? = null

    override fun saveSession(session: UserSession) {
        userSession = session
        prefs.saveUserId(session.userId)
    }

    override fun hasSession() = userSession != null

    override fun restoreSession() {
        val userId = prefs.getUserId()

        if (userId != null) {
            userSession = UserSession(userId)
        }
    }

    override fun getSession() = userSession
}
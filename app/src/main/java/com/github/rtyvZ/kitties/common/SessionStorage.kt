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
        val secretKey = prefs.getSecretKey()
        val encryptedKey = prefs.getEncryptKey()

        if (userId != null) {
            userSession =
                secretKey?.let {
                    encryptedKey?.let { secretKey ->
                        UserSession(
                            userId,
                            it,
                            secretKey
                        )
                    }
                }
        }
    }

    override fun getSession() = userSession
    override fun saveEncryptedToken(encryptedKey: String) {
        if (prefs.getEncryptKey().isNullOrBlank()) {
            prefs.saveEncryptKey(encryptedKey)
        }
    }

    override fun saveSecretKey(secretKey: String) {
        if (prefs.getSecretKey().isNullOrBlank()) {
            prefs.saveSecretKey(secretKey)
        }
    }
}
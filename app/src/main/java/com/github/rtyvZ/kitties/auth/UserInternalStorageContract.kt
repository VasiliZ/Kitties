package com.github.rtyvZ.kitties.auth

import com.github.rtyvZ.kitties.common.models.UserSession

interface UserInternalStorageContract {
    fun saveSession(session: UserSession)
    fun hasSession(): Boolean
    fun restoreSession()
    fun getSession(): UserSession?
}
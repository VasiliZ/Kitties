package com.github.rtyvZ.kitties.auth

interface UserInternalStorageContract {
    fun saveSession(session: UserSession)
    fun hasSession(): Boolean
    fun restoreSession()
    fun getSession(): UserSession?
}
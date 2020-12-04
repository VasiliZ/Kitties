package com.github.rtyvZ.kitties.auth

interface ProvideUserContract {
    fun saveSession(session: UserSession)
    fun hasSession(): Boolean
    fun restoreSession()
    fun getSession(): UserSession?
}
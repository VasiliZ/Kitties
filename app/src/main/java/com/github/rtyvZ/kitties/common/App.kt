package com.github.rtyvZ.kitties.common

import android.app.Application
import android.content.Context
import com.github.rtyvZ.kitties.auth.UserSession
import com.github.rtyvZ.kitties.extentions.getUserId
import com.github.rtyvZ.kitties.extentions.saveUserId
import com.github.rtyvZ.kitties.network.ConnectionChecker

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    object ApiKeyProvider {
        fun getKey() = "0506e17c-e910-4a31-87ab-f68eace36b7d"
    }

    object ConnectionCheckerProvider {
        fun checkConnection() = ConnectionChecker(context)
    }

    object ResourceProvider {

        private val context = App.context

        fun getString(resourceId: Int) = context.getString(resourceId)
    }

    object SessionStorage {
        private const val SESSION_STORAGE = "SESSION_STORAGE"
        private val context = App.context
        private var userSession: UserSession? = null

        private fun getSp() =
            context.getSharedPreferences(SESSION_STORAGE, Context.MODE_PRIVATE)

        fun saveSession(session: UserSession) {
            userSession = session
            getSp().saveUserId(session.userId)
        }

        fun hasSession() = userSession != null

        fun restoreSession() {
            val userId = getSp().getUserId()

            if (userId != null) {
                userSession = UserSession(userId)
            }
        }

        fun getSession() = userSession
    }

    companion object {

        private lateinit var context: Context

    }
}
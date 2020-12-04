package com.github.rtyvZ.kitties

import android.content.Context
import com.github.rtyvZ.kitties.auth.UserSession
import com.github.rtyvZ.kitties.dataBase.CatDatabase
import com.github.rtyvZ.kitties.di.DaggerAppComponent
import com.github.rtyvZ.kitties.extentions.getUserId
import com.github.rtyvZ.kitties.extentions.saveUserId
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
    private lateinit var injector: AndroidInjector<App>

    override fun onCreate() {
        injector = DaggerAppComponent.builder().application(this).build()
        super.onCreate()
        context = applicationContext
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return injector
    }

    object DataBaseProvider {
        fun getDataBase() =
            CatDatabase.getDatabase(context)
    }

    object ApiKeyProvider {
        fun getKey() = "0506e17c-e910-4a31-87ab-f68eace36b7d"
    }

    object ResourceProvider {

        private val context = Companion.context

        fun getString(resourceId: Int) = context.getString(resourceId)
    }

    object SessionStorage {
        private const val SESSION_STORAGE = "SESSION_STORAGE"
        private val context = Companion.context
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
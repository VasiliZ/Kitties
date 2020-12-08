package com.github.rtyvZ.kitties

import android.content.Context
import com.github.rtyvZ.kitties.db.CatDatabase
import com.github.rtyvZ.kitties.di.DaggerAppComponent
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

    companion object {

        private lateinit var context: Context

    }
}
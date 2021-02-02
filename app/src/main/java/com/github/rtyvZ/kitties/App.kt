package com.github.rtyvZ.kitties

import android.content.Context
import com.github.rtyvZ.kitties.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
    private lateinit var injector: AndroidInjector<App>

    override fun onCreate() {
        injector = DaggerAppComponent.builder().application(this).build()
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return injector
    }
}
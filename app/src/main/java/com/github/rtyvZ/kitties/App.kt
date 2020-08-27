package com.github.rtyvZ.kitties

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    object ApiKeyProvider {
        fun getKey() = "0506e17c-e910-4a31-87ab-f68eace36b7d"
    }
}
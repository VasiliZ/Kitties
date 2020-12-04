package com.github.rtyvZ.kitties.network

import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.R

class NoConnectivityException : Throwable() {
    private val recourseProvider = App.ResourceProvider

    override val message: String?
        get() =
            recourseProvider.getString(R.string.no_connection)
}
package com.github.rtyvZ.kitties.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.common.App

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(app) as T
    }
}
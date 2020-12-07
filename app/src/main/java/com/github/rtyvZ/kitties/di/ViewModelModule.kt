package com.github.rtyvZ.kitties.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.auth.LaunchViewModel
import com.github.rtyvZ.kitties.base.ViewModelFactory
import com.github.rtyvZ.kitties.ui.main.MainActivityViewModel
import com.github.rtyvZ.kitties.ui.randomCats.RandomCatsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(launchViewModel: LaunchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RandomCatsViewModel::class)
    abstract fun bindRandomCatsViewModel(randomCatsViewModel: RandomCatsViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun funBindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
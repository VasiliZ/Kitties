package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.auth.LaunchActivity
import com.github.rtyvZ.kitties.ui.main.MainActivity
import com.github.rtyvZ.kitties.ui.randomCats.RandomCatsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {

    @ContributesAndroidInjector(modules = [LaunchActivityModule::class])
    internal abstract fun bindLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [RandomCatsFragmentModule::class])
    internal abstract fun bindRandomCatsFragment(): RandomCatsFragment
}
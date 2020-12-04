package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.auth.LaunchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {

    @ContributesAndroidInjector(modules = [LaunchActivityModule::class])
    internal abstract fun bindLaunchActivity(): LaunchActivity
}
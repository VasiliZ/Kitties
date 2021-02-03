package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.SessionStorage
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class UserStorageModule {

    @Binds
    abstract fun bindsUserStorageContract(sessionStorage: SessionStorage): UserInternalStorageContract
}
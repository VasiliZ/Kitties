package com.github.rtyvZ.kitties.di

import android.content.Context
import com.github.rtyvZ.kitties.App
import com.github.rtyvZ.kitties.auth.ProvideUserContract
import com.github.rtyvZ.kitties.auth.SessionStorage
import com.github.rtyvZ.kitties.common.AppPreference
import com.github.rtyvZ.kitties.common.PreferenceHelper
import com.github.rtyvZ.kitties.common.PreferenceInfo
import com.github.rtyvZ.kitties.common.Strings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProvideContextModule {

    @Provides
    internal fun provideContext(app: App): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    internal fun providePreferences(preference: AppPreference): PreferenceHelper {
        return preference
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceFileName(): String {
        return Strings.PreferenceConst.AUTH_USER_PREFERENCE
    }

    @Provides
    @Singleton
    internal fun provideSessionStorage(preference: AppPreference): ProvideUserContract {
        return SessionStorage(preference)
    }
}
package com.github.rtyvZ.kitties.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    internal fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    internal fun providesExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }
}
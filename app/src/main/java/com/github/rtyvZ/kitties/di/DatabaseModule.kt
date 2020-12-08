package com.github.rtyvZ.kitties.di

import android.content.Context
import androidx.room.Room
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.db.CatDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun providesDB(context: Context): CatDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CatDatabase::class.java,
            Strings.Const.DB_NAME
        ).build()
    }
}
package com.github.rtyvZ.kitties.di

import android.content.Context
import com.github.rtyvZ.kitties.ui.main.ImageHelper
import com.github.rtyvZ.kitties.ui.sendPhoto.TakeAPhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TakeAPhotoModule {

    @Provides
    @Singleton
    fun provideTakeAPhotoRepository(
        imageHelper: ImageHelper,
        @ApplicationContext context: Context
    ): TakeAPhotoRepository {
        return TakeAPhotoRepository(imageHelper, context)
    }

    @Provides
    fun providesImageHelper(): ImageHelper {
        return ImageHelper()
    }
}
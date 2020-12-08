package com.github.rtyvZ.kitties.di

import android.content.Context
import com.github.rtyvZ.kitties.ui.main.ImageHelper
import com.github.rtyvZ.kitties.ui.sendPhoto.TakeAPhotoRepository
import dagger.Module
import dagger.Provides

@Module
class TakeAPhotoModule {

    @Provides
    internal fun provideTakeAPhotoRepo(
        imageHelper: ImageHelper,
        context: Context
    ): TakeAPhotoRepository {
        return TakeAPhotoRepository(imageHelper, context)
    }
}
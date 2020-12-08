package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.ui.myUploadedCats.MyCatsModel
import com.github.rtyvZ.kitties.ui.myUploadedCats.MyCatsRepository
import dagger.Module
import dagger.Provides

@Module
class MyCatsModule {

    @Provides
    internal fun provideMyCatsModel(myCatsRepository: MyCatsRepository): MyCatsModel {
        return MyCatsModel(myCatsRepository)
    }
}
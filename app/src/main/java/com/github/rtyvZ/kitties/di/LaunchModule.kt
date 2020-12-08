package com.github.rtyvZ.kitties.di

import android.content.Context
import com.github.rtyvZ.kitties.domain.randomCat.RandomCatsModel
import com.github.rtyvZ.kitties.repositories.RandomCatsRepository.RandomCatsRepository
import com.github.rtyvZ.kitties.ui.main.MainRepository
import dagger.Module
import dagger.Provides

@Module
class LaunchModule {

    @Provides
    internal fun provideMainRepository(context: Context): MainRepository {
        return MainRepository(context)
    }

    @Provides
    internal fun provideRandomModel(randomCatsRepository: RandomCatsRepository): RandomCatsModel {
        return RandomCatsModel(randomCatsRepository)
    }
}
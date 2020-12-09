package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.ui.breedCats.BreedCatsModel
import com.github.rtyvZ.kitties.ui.breedCats.BreedCatsRepository
import dagger.Module
import dagger.Provides

@Module
class BreedCatsFragmentModule {

    @Provides
    internal fun providesBreedCatsModel(breedCatsRepository: BreedCatsRepository): BreedCatsModel {
        return BreedCatsModel(breedCatsRepository)
    }
}
package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.ui.catsBreeds.BreedsModel
import com.github.rtyvZ.kitties.ui.catsBreeds.BreedsRepository
import dagger.Module
import dagger.Provides

@Module
class BreedsModule {

    @Provides
    internal fun provideBreedsModel(breedCatsRepository:BreedsRepository):BreedsModel{
        return BreedsModel(breedCatsRepository)
    }
}
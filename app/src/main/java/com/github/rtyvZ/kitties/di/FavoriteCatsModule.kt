package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.domain.favoriteCats.FavoriteCatsModel
import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsRepository
import dagger.Module
import dagger.Provides

@Module
class FavoriteCatsModule {

    @Provides
    internal fun provideFavoriteCatsModel(favoriteCatsRepository: FavoriteCatsRepository): FavoriteCatsModel {
        return FavoriteCatsModel(favoriteCatsRepository)
    }
}
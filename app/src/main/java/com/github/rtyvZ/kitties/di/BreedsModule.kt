package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.repositories.catBreeds.PagingBreedsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BreedsModule {
    @Provides
    @ViewModelScoped
    fun provideBreedsPagingRepo(api: Api): PagingBreedsRepo {
        return PagingBreedsRepo(api)
    }
}
package com.github.rtyvZ.kitties.diUserInternalStorageContract

import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.repositories.favoriteCats.FavoriteCatsPagingRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object FavoriteCatsModule {
    @Provides
    @ViewModelScoped
    fun providePagingRepo(api: Api, sessionStorage: UserInternalStorageContract): FavoriteCatsPagingRepo {
        return FavoriteCatsPagingRepo(api, sessionStorage)
    }
}
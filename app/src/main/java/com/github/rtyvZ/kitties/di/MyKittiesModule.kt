package com.github.rtyvZ.kitties.di

import com.github.rtyvZ.kitties.db.MyCatsDao
import com.github.rtyvZ.kitties.repositories.myKitties.MyKittiesPagingRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MyKittiesModule {
    @Provides
    @ViewModelScoped
    fun providePagingRepo(dao: MyCatsDao): MyKittiesPagingRepo {
        return MyKittiesPagingRepo(dao)
    }
}
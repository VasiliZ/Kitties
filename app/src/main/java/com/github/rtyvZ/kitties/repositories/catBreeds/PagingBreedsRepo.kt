package com.github.rtyvZ.kitties.repositories.catBreeds

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.models.CatBreed
import kotlinx.coroutines.flow.Flow

class PagingBreedsRepo(val api: Api) {

    fun fetchBreeds(): Flow<PagingData<CatBreed>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true, prefetchDistance = 0)
        ) {
            CatsBreedsDataSource(api, PAGE_SIZE)
        }.flow
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}
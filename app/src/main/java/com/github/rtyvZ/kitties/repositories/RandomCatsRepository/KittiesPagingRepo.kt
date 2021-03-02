package com.github.rtyvZ.kitties.repositories.RandomCatsRepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.network.data.CatResponse
import kotlinx.coroutines.flow.Flow

class KittiesPagingRepo(private val api: Api) {

    fun fetchKitties(): Flow<PagingData<CatResponse>> {
        return Pager(
            PagingConfig(pageSize = 50, enablePlaceholders = false, prefetchDistance = 2)
        ) {
            KittySource(api)
        }.flow
    }
}
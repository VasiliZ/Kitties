package com.github.rtyvZ.kitties.repositories.randomCatsRepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.SessionStorage
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.models.Cat
import com.github.rtyvZ.kitties.network.data.CatResponse
import kotlinx.coroutines.flow.Flow

class KittiesPagingRepo(private val api: Api, private val sessionStorage: UserInternalStorageContract) {

    fun fetchKitties(): Flow<PagingData<Cat>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = 1
            )
        ) {
            KittySource(api, sessionStorage)
        }.flow
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}
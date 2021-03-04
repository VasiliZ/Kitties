package com.github.rtyvZ.kitties.repositories.favoriteCats

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse
import kotlinx.coroutines.flow.Flow

class FavoriteCatsPagingRepo(
    private val api: Api,
    private val sessionStorage: UserInternalStorageContract
) {
    fun fetchFavoriteKitties(): Flow<PagingData<FavoriteCatsResponse>> {
        return Pager(
            PagingConfig(
                pageSize = 15,
                enablePlaceholders = false,
                prefetchDistance = 1
            )
        ) {
            FavoriteCatsPaging(api, sessionStorage)
        }.flow
    }
}
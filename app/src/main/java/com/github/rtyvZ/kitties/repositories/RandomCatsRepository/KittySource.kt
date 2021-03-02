package com.github.rtyvZ.kitties.repositories.RandomCatsRepository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.data.CatResponse

class KittySource(private val api: Api) : PagingSource<Int, CatResponse>() {

    private var prevKey = 0
    private var nextKey = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatResponse> {
        return when (val response = api.getListKitties(1)) {
            is NetworkResponse.Success -> {
                changeKeys()
                LoadResult.Page(
                    data = response.body.map { it },
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            is NetworkResponse.ApiError -> {
                LoadResult.Error(Throwable())
            }
            is NetworkResponse.UnknownError -> {
                LoadResult.Error(response.error!!)
            }
            is NetworkResponse.NetworkError -> {
                LoadResult.Error(response.error)
            }
        }
    }

    private fun changeKeys() {
        prevKey = nextKey
        nextKey++
    }

    override fun getRefreshKey(state: PagingState<Int, CatResponse>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }
}
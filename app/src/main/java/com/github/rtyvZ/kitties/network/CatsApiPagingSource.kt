package com.github.rtyvZ.kitties.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.network.data.CatResponse

class CatsApiPagingSource(private val api: Api) :
    PagingSource<Int, CatResponse>() {
    override fun getRefreshKey(state: PagingState<Int, CatResponse>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatResponse> {
        val nextPage = params.key ?: 1
        val response = api.getListKitties(nextPage)
        return when (response) {
            is NetworkResponse.Success -> {
                LoadResult.Page(response.body, null, nextPage)
            }
            is NetworkResponse.NetworkError -> {
                LoadResult.Error(response.error)
            }
            is NetworkResponse.UnknownError -> {
                LoadResult.Error(response.error!!)
            }
            is NetworkResponse.ApiError -> {
                LoadResult.Error(Throwable())
            }
        }
    }

}
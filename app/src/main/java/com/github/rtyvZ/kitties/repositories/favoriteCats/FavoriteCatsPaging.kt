package com.github.rtyvZ.kitties.repositories.favoriteCats

import androidx.paging.PagingState
import com.github.rtyvZ.kitties.common.Api
import com.github.rtyvZ.kitties.common.UserInternalStorageContract
import com.github.rtyvZ.kitties.common.repository.BaseDataSource
import com.github.rtyvZ.kitties.network.NetworkResponse
import com.github.rtyvZ.kitties.network.response.FavoriteCatsResponse

class FavoriteCatsPaging(
    private val api: Api,
    private val sessionStorage: UserInternalStorageContract,
) : BaseDataSource<Int, FavoriteCatsResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FavoriteCatsResponse>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteCatsResponse> {

        sessionStorage.getSession()?.let {
            return when (val response = api.getFavoritesCat(it.userId, 15, nextKey)) {
                is NetworkResponse.Success -> {
                    if (response.body.isNotEmpty()) {
                        changeKey()
                    } else {
                        nextKey = null
                    }
                    LoadResult.Page(
                        data = response.body.map { it },
                        prevKey = null,
                        nextKey
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
                else -> {
                    LoadResult.Error(Throwable())
                }
            }
        }
        return LoadResult.Error(Throwable("You not have got a session"))
    }
}
